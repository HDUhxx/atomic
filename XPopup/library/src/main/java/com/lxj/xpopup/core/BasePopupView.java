package com.lxj.xpopup.core;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.BlurAnimator;
import com.lxj.xpopup.animator.EmptyAnimator;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.animator.ScaleAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.animator.TranslateAlphaAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.util.ComponentUtil;
import com.lxj.xpopup.util.EventUtil;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.ShadowLayout;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.components.TextField;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Window;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.system.version.SystemVersion;

import java.util.ArrayList;

import static com.lxj.xpopup.enums.PopupAnimation.NoAnimation;

/**
 * Description: 弹窗基类
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends StackLayout implements Component.TouchEventListener {

    public PopupInfo popupInfo;
    protected PopupAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    protected BlurAnimator blurAnimator;
    private final int touchSlop = 24; // 触发移动事件的最小距离
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    protected boolean isCreated = false;
    protected ShadowLayout shadowLayout;
    private boolean hasMoveUp = false;
    protected EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());
    protected static BasePopupView basePopupView;

    public BasePopupView(Context context, PopupInfo popupInfo) {
        super(context);
        if (context instanceof AbilityPackage) {
            throw new IllegalArgumentException("XPopup的Context不可以是AbilityPackage类型！");
        }
        ComponentContainer.LayoutConfig lp_basepop = getLayoutConfig();
        lp_basepop.width = ComponentContainer.LayoutConfig.MATCH_PARENT;
        lp_basepop.height = ComponentContainer.LayoutConfig.MATCH_PARENT;
        setLayoutConfig(lp_basepop);
        this.popupInfo = popupInfo;

        // 添加Popup窗体内容View
        Component contentView = LayoutScatter.getInstance(context).parse(getInnerLayoutId(), this, false);
        if (popupInfo != null && getMaxWidth() != 0) {
            ComponentContainer.LayoutConfig layoutConfig = contentView.getLayoutConfig();
            layoutConfig.width = getMaxWidth();
            contentView.setLayoutConfig(layoutConfig);
        }
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象
        contentView.setAlpha(0);
        setVisibility(INVISIBLE);
        addComponent(contentView);
        setTouchEventListener(this);
        basePopupView = this;
    }

    public BasePopupView show() {
        if (popupInfo == null) {
            return this;
        }
        if (popupStatus == PopupStatus.Showing || popupStatus == PopupStatus.Dismissing) {
            return this;
        }
        popupStatus = PopupStatus.Showing;
        if (popupInfo.isRequestFocus && popupInfo.contentRoot != null) {
            // 显示弹窗之前，应该隐藏输入法
            KeyboardUtils.hideSoftInput(popupInfo.contentRoot);
        }
        if (!popupInfo.isComponentMode && dialog != null && dialog.isShowing()) {
            return BasePopupView.this;
        }
        handler.postTask(attachTask);
        return this;
    }

    private final Runnable attachTask = new Runnable() {
        @Override
        public void run() {
            // 1. add PopupView to its dialog.
            attachToHost();

            // 2.注册对话框监听器
            KeyboardUtils.registerSoftInputChangedListener(BasePopupView.this, new KeyboardUtils.OnSoftInputChangedListener() {
                @Override
                public void onSoftInputChanged(int height) {
                    if (popupInfo != null && popupInfo.xPopupCallback != null) {
                        popupInfo.xPopupCallback.onKeyBoardStateChanged(BasePopupView.this, height);
                    }
                    if (height == 0) { // 说明对话框隐藏
                        XPopupUtils.moveDown(BasePopupView.this);
                    } else {
                        // when show keyboard,move up
                        // 全屏弹窗特殊处理，等show之后再移动
                        if (BasePopupView.this instanceof FullScreenPopupView && popupStatus == PopupStatus.Showing) {
                            return;
                        }
                        if (BasePopupView.this instanceof PartShadowPopupView && popupStatus == PopupStatus.Showing) {
                            return;
                        }
                        XPopupUtils.moveUpToKeyboard(height, BasePopupView.this);
                        hasMoveUp = true;
                    }
                }
            });

            // 3.do init,game start.
            init();
        }
    };

    public FullScreenDialog dialog;

    private void attachToHost() {
        if (popupInfo == null) {
            throw new IllegalArgumentException("如果弹窗对象是复用的，则不要设置isDestroyOnDismiss(true)");
        }
        if (popupInfo.isComponentMode) {
            // component实现
            ComponentContainer decorView = (ComponentContainer) popupInfo.contentRoot.getComponentParent();
            popupInfo.decorView = decorView;
            decorView.addComponent(this);
            if (!popupInfo.isClickThrough) {
                this.setClickedListener(component -> {
                });
            }
        } else {
            // dialog实现
            if (dialog == null) {
                dialog = new FullScreenDialog(getContext())
                        .setContent(this);
            }
            if (shadowLayout != null) {
                shadowLayout.invalidate(); // 复用弹窗时，需要再次调用绘制才有阴影
            }
            dialog.show();
        }
        if (popupInfo.keepScreenOn) {
            getHostWindow().addFlags(WindowManager.LayoutConfig.MARK_SCREEN_ON_ALWAYS);
        }
    }

    /**
     * 执行初始化
     */
    protected void init() {
        // 2. 收集动画执行器
        if (shadowBgAnimator == null) {
            shadowBgAnimator = new ShadowBgAnimator(this, getAnimationDuration(), getShadowBgColor());
        }
        if (popupInfo.hasBlurBg) {
            blurAnimator = new BlurAnimator(this, getShadowBgColor());
            blurAnimator.hasShadowBg = popupInfo.hasShadowBg;
        }

        // 1. 初始化Popup
        if (this instanceof AttachPopupView || this instanceof BubbleAttachPopupView ||
                this instanceof PartShadowPopupView || this instanceof PositionPopupView) {
            initPopupContent();
        } else if (!isCreated) {
            initPopupContent();
        }
        if (!isCreated) {
            isCreated = true;
            onCreate();
            if (popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onCreated(this);
            }
        }
        handler.postTask(initTask, 10);
    }

    private final Runnable initTask = new Runnable() {
        @Override
        public void run() {
            if (getHostWindow() == null) {
                return;
            }

            if (popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.beforeShow(BasePopupView.this);
            }
            beforeShow();

            // 由于Attach弹窗有个位置设置过程，需要在位置设置完毕自己开启动画
            if (!(BasePopupView.this instanceof AttachPopupView) && !(BasePopupView.this instanceof BubbleAttachPopupView)
                    && !(BasePopupView.this instanceof PositionPopupView)) {
                initAnimator();

                doShowAnimation();

                doAfterShow();
            }
            // 必须在控件VISABLE之后再设置onKey监听才能生效
            focusAndProcessBackPress();
        }
    };

    protected void initAnimator() {
        getPopupContentView().setAlpha(1f);
        setVisibility(VISIBLE);
        // 优先使用自定义的动画器
        if (popupInfo.customAnimator != null) {
            popupContentAnimator = popupInfo.customAnimator;
            popupContentAnimator.targetView = getPopupContentView();
        } else {
            // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
            popupContentAnimator = genAnimatorByPopupType();
            if (popupContentAnimator == null) {
                popupContentAnimator = getPopupAnimator();
            }
        }

        // 3. 初始化动画执行器
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && popupInfo.isComponentMode) {
            shadowBgAnimator.initAnimator();
        }
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.initAnimator();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.initAnimator();
        }

    }

    private void detachFromHost() {
        if (popupInfo != null && popupInfo.isComponentMode) {
            ComponentContainer decorView = (ComponentContainer) getComponentParent();
            if (decorView != null) {
                decorView.removeComponent(this);
            }
        } else {
            dialog.hide();
        }
        onDetachedFromWindow();
    }

    public Window getHostWindow() {
        if (popupInfo != null && popupInfo.isComponentMode) {
            Ability ability;
            Context context = popupInfo.contentRoot.getContext();
            if (context instanceof AbilitySlice) {
                ability = ((AbilitySlice) context).getAbility();
            } else if (context instanceof Fraction) {
                ability = ((Fraction) context).getFractionAbility();
            } else {
                ability = (Ability) context;
            }
            return ability.getWindow();
        } else {
            return dialog == null ? null : dialog.getWindow();
        }
    }

    protected ComponentContainer getWindowDecorView() {
        if (popupInfo.contentRoot != null) {
            return ComponentUtil.getDecorView(popupInfo.contentRoot);
        } else {
            return null;
        }
    }

    protected void doAfterShow() {
        handler.removeTask(doAfterShowTask);
        handler.postTask(doAfterShowTask, getAnimationDuration());
    }

    private Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Show;
            onShow();

            if (popupInfo != null && popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onShow(BasePopupView.this);
            }
            // 再次检测移动距离
            if (getHostWindow() != null && KeyboardUtils.getSoftInputHeight(BasePopupView.this) > 0 && !hasMoveUp) {
                XPopupUtils.moveUpToKeyboard(KeyboardUtils.getSoftInputHeight(BasePopupView.this), BasePopupView.this);
            }
        }
    };

    private ShowSoftInputTask showSoftInputTask;

    public void focusAndProcessBackPress() {
        if (popupInfo != null && popupInfo.isRequestFocus) {
            setTouchFocusable(true);
            if (SystemVersion.getApiVersion() >= 7) {
                // API>=7时，具有焦点的控件会有蓝色边框，调用此方法可以不显示蓝色边框
                setFocusedEffect(false);
            }
            requestFocus();
            // 此处焦点可能被内部的EditText抢走，也需要给EditText也设置返回按下监听

            setKeyEventListener(new BackPressListener());
            if (!popupInfo.autoFocusEditText) {
                showSoftInput(this);
            }

            // let all EditText can process back pressed.
            ArrayList<TextField> list = new ArrayList<>();
            XPopupUtils.findAllEditText(list, (ComponentContainer) getPopupContentView());
            for (int i = 0; i < list.size(); i++) {
                final TextField et = list.get(i);
                // 给输入框设置返回按键监听无效，暂未找到原因
                et.setKeyEventListener(new BackPressListener());
                if (i == 0 && popupInfo.autoFocusEditText) {
                    et.setFocusable(FOCUS_ENABLE);
                    et.setTouchFocusable(true);
                    if (SystemVersion.getApiVersion() >= 7) {
                        setFocusedEffect(false);
                    }
                    et.requestFocus();
                    if (popupInfo.autoOpenSoftInput) {
                        showSoftInput(et);
                    }
                }
            }
        }
    }

    protected void showSoftInput(Component focusView) {
        if (showSoftInputTask == null) {
            showSoftInputTask = new ShowSoftInputTask(focusView);
        } else {
            handler.removeTask(showSoftInputTask);
        }
        handler.postTask(showSoftInputTask, 10);
    }


    protected void dismissOrHideSoftInput() {
        if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0) {
            dismiss();
        } else {
            KeyboardUtils.hideSoftInput(BasePopupView.this);
        }
    }

    static class ShowSoftInputTask implements Runnable {
        Component focusView;

        public ShowSoftInputTask(Component focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null) {
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    class BackPressListener implements KeyEventListener {
        @Override
        public boolean onKeyEvent(Component component, KeyEvent event) {
            if (event.isKeyDown()) {
                if (event.getKeyCode() == KeyEvent.KEY_BACK && popupInfo != null) {
                    if (popupInfo.isDismissOnBackPressed &&
                            (popupInfo.xPopupCallback == null || !popupInfo.xPopupCallback.onBackPressed(BasePopupView.this))) {
                        dismissOrHideSoftInput();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     *
     * @return 弹窗要执行的动画执行器
     */
    protected PopupAnimator genAnimatorByPopupType() {
        if (popupInfo == null || popupInfo.popupAnimation == null) {
            return null;
        }
        switch (popupInfo.popupAnimation) {
            case ScaleFromCenter:
            case ScaleFromLeftTop:
            case ScaleFromRightTop:
            case ScaleFromLeftBottom:
            case ScaleFromRightBottom:
                return new ScaleAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);
            case NoAnimation:
                return new EmptyAnimator(getPopupContentView(), getAnimationDuration());
            default:
                break;
        }
        return null;
    }

    /**
     * 获取弹窗外层布局资源id
     *
     * @return 弹窗外层布局资源id
     */
    protected abstract int getInnerLayoutId();

    /**
     * 如果你自己继承BasePopupView来做，这个不用实现
     *
     * @return 弹窗内层布局资源id
     */
    protected int getImplLayoutId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，用于每种类型的PopupView自定义自己的动画器
     *
     * @return 动画执行器
     */
    protected PopupAnimator getPopupAnimator() {
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，不要去重写。
     */
    protected void initPopupContent() {
    }

    /**
     * do init.
     */
    protected void onCreate() {
    }

    protected void applyDarkTheme() {
    }

    protected void applyLightTheme() {
    }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (popupInfo == null) {
            return;
        }
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && shadowBgAnimator != null && popupInfo.isComponentMode) {
            shadowBgAnimator.animateShow();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateShow();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.animateShow();
        }
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (popupInfo == null) {
            return;
        }
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && shadowBgAnimator != null && popupInfo.isComponentMode) {
            shadowBgAnimator.animateDismiss();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateDismiss();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.animateDismiss();
        }
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。
     * 而且我们对PopupView执行的动画，也是对它执行的动画
     *
     * @return 获取弹窗的内容控件
     */
    public Component getPopupContentView() {
        return getComponentAt(0);
    }

    public Component getPopupImplView() {
        Component imlView = ((ComponentContainer) getPopupContentView()).getComponentAt(0);
        if (imlView instanceof ShadowLayout) {
            return ((ShadowLayout) imlView).getComponentAt(0);
        } else {
            return imlView;
        }
    }

    public int getAnimationDuration() {
        if (popupInfo == null) {
            return 0;
        }
        if (popupInfo.popupAnimation == NoAnimation) {
            return 1;
        }
        return popupInfo.animationDuration >= 0 ? popupInfo.animationDuration : XPopup.getAnimationDuration() + 1;
    }

    public int getShadowBgColor() {
        return popupInfo != null && popupInfo.shadowBgColor != 0 ? popupInfo.shadowBgColor : XPopup.getShadowBgColor();
    }

    /**
     * 弹窗的最大宽度，用来限制弹窗的最大宽度
     * 返回0表示不限制，默认为0
     *
     * @return 弹窗的最大宽度
     */
    protected int getMaxWidth() {
        return popupInfo.maxWidth;
    }

    /**
     * 弹窗的最大高度，用来限制弹窗的最大高度
     * 返回0表示不限制，默认为0
     *
     * @return 弹窗的最大高度
     */
    protected int getMaxHeight() {
        return popupInfo.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     * 返回0表示不设置，默认为0
     *
     * @return 弹窗的宽度
     */
    protected int getPopupWidth() {
        return popupInfo.popupWidth;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     * 返回0表示不设置，默认为0
     *
     * @return 弹窗的高度
     */
    protected int getPopupHeight() {
        return popupInfo.popupHeight;
    }

    /**
     * 消失
     */
    public void dismiss() {
        handler.removeTask(attachTask);
        handler.removeTask(initTask);
        if (popupStatus == PopupStatus.Dismissing || popupStatus == PopupStatus.Dismiss) {
            return;
        }
        popupStatus = PopupStatus.Dismissing;
        clearFocus();
        if (popupInfo != null && popupInfo.xPopupCallback != null) {
            popupInfo.xPopupCallback.beforeDismiss(this);
        }
        beforeDismiss();
        doDismissAnimation();
        doAfterDismiss();
    }

    /**
     * 会等待弹窗show动画执行完毕再消失
     */
    public void smartDismiss() {
        handler.postTask(new Runnable() {
            @Override
            public void run() {
                delayDismiss(getAnimationDuration() + 50);
            }
        });
    }

    public void delayDismiss(long delay) {
        if (delay < 0) {
            delay = 0;
        }
        handler.postTask(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        // PartShadowPopupView要等到完全关闭再关闭输入法，不然有问题
        if (popupInfo != null && popupInfo.autoOpenSoftInput && !(this instanceof PartShadowPopupView)) {
            KeyboardUtils.hideSoftInput(this);
        }
        handler.removeTask(doAfterDismissTask);
        handler.postTask(doAfterDismissTask, getAnimationDuration());
    }

    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Dismiss;
            if (popupInfo == null) {
                return;
            }
            if (popupInfo.autoOpenSoftInput && BasePopupView.this instanceof PartShadowPopupView) {
                KeyboardUtils.hideSoftInput(BasePopupView.this);
            }
            onDismiss();
            XPopup.longClickPoint = null;
            if (popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onDismiss(BasePopupView.this);
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null; // no cache, avoid some bad edge effect.
            }

            if (popupInfo.isRequestFocus) {
                // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                ComponentContainer windowDecorView = getWindowDecorView();
                if (windowDecorView != null) {
                    windowDecorView.setFocusable(FOCUS_ADAPTABLE);
                    windowDecorView.setTouchFocusable(true);
                }
            }

            // 移除弹窗，GameOver
            detachFromHost();
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }

    public boolean isShow() {
        return popupStatus != PopupStatus.Dismiss;
    }

    public boolean isDismiss() {
        return popupStatus == PopupStatus.Dismiss;
    }

    public void toggle() {
        if (isShow()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * onDismiss之前执行一次
     */
    protected void beforeDismiss() {
    }

    protected void beforeShow() {
    }

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() {
    }

    public void destroy() {
        if (popupInfo != null) {
            popupInfo.atView = null;
            popupInfo.watchView = null;
            popupInfo.xPopupCallback = null;
            if (popupInfo.isDestroyOnDismiss) {
                popupInfo = null;
            }
        }
        if (dialog != null) {
            dialog.contentView = null;
            dialog = null;
        }
        if (blurAnimator != null && blurAnimator.decorBitmap != null) {
            if (!blurAnimator.decorBitmap.isReleased()) {
                blurAnimator.decorBitmap.release();
                blurAnimator.decorBitmap = null;
            }
        }
        if (basePopupView != null) {
            basePopupView = null;
        }
    }

    public void onDetachedFromWindow() {
        handler.removeAllEvent();
        if (popupInfo != null) {
            KeyboardUtils.removeLayoutChangeListener(BasePopupView.this);
            if (popupInfo.isDestroyOnDismiss) { // 如果开启isDestroyOnDismiss，强制释放资源
                if (!popupInfo.isComponentMode && dialog != null) {
                    dialog.destroy();
                }
                destroy();
            }
        }
        popupStatus = PopupStatus.Dismiss;
        showSoftInputTask = null;
    }

    private float x;
    private float y;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = getGlobalVisibleRect(getPopupImplView());

        if (!XPopupUtils.isInRect(EventUtil.getX(event), EventUtil.getY(event), rect)) {
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    x = EventUtil.getX(event);
                    y = EventUtil.getY(event);
                    passClickThrough(event);
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                case TouchEvent.CANCEL:
                    float dx = EventUtil.getX(event) - x;
                    float dy = EventUtil.getY(event) - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (!XPopupUtils.isInRect(EventUtil.getX(event), EventUtil.getY(event), rect)) {
                        passClickThrough(event);
                    }
                    if (distance < touchSlop && popupInfo != null && popupInfo.isDismissOnTouchOutside) {
                        if (!(this instanceof ImageViewerPopupView)) {
                            dismiss();
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    /**
     * 获取控件的显示区域（相对于根容器）
     *
     * @param component 控件
     * @return 存储了控件显示区域的Rect
     */
    private Rect getGlobalVisibleRect(Component component) {
        return ComponentUtil.getLocationInDecorView(component);
    }

    private void passClickThrough(TouchEvent event) {
        if (popupInfo != null && popupInfo.isClickThrough) {
            if (!popupInfo.isComponentMode) {
                // Dialog实现模式的事件穿透
            }
        }
    }

}
