package com.ohos.trebleshot.popupwindow;

import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.aafwk.ability.Lifecycle;
import ohos.aafwk.ability.LifecycleStateObserver;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class PopupWindowView extends StackLayout implements Component.BindStateChangedListener, Component.TouchEventListener, LifecycleStateObserver {
    ListContainer listContainer;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    public PopupEntity popupEntity;
    protected PopupAnimator popupContentAnimator;
    private int touchSlop = 24; // 触发移动事件的最小距离
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    protected boolean isCreated = false;
    private EventHandler handler = new EventHandler(EventRunner.getMainEventRunner());

    /**
     * @param context          上下文
     * @param bindLayoutId     layoutId 要求layoutId中必须有一个id为listContainer的ListContainer
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的Image，和id为tv_text的Text
     */
    public PopupWindowView(Context context, int bindLayoutId, int bindItemLayoutId, PopupEntity popupEntity) {
        super(context);
        this.popupEntity = popupEntity;
        init1(context);
        attachPopupContainer = (PartShadowContainer) findComponentById(ResourceTable.Id_attachPopupContainer);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    protected int getPopupLayoutId() {
        return ResourceTable.Layout__xpopup_attach_popup_view;
    }

    private void init1(Context context) {
        if (context instanceof AbilityPackage) {
            throw new IllegalArgumentException("XPopup的Context必须是Ability类型！");
        }

        // 添加Popup窗体内容View
        Component contentView = LayoutScatter.getInstance(context).parse(getPopupLayoutId(), this, false);
        if (popupEntity != null && getMaxWidth() != 0) {
            ComponentContainer.LayoutConfig layoutConfig = contentView.getLayoutConfig();
            layoutConfig.width = getMaxWidth();
            contentView.setLayoutConfig(layoutConfig);
        }
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        contentView.setAlpha(0);
        setVisibility(INVISIBLE);
        addComponent(contentView);
        setTouchEventListener(this);
        setBindStateChangedListener(this);
    }

    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? ResourceTable.Layout__xpopup_attach_impl_list : bindLayoutId;
    }

    protected void onCreate() {
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);
        final BaseProvider<String> adapter = new BaseProvider<String>(getContext(), Arrays.asList(data), bindItemLayoutId == 0 ? ResourceTable.Layout__xpopup_adapter_text : bindItemLayoutId) {
            @Override
            protected void bind(ViewHolder holder, String itemData, int position) {
                holder.setText(ResourceTable.Id_tv_text, itemData);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(VISIBLE);
                    holder.<Image>getView(ResourceTable.Id_iv_image).setPixelMap(iconIds[position]);
                } else {
                    holder.getView(ResourceTable.Id_iv_image).setVisibility(HIDE);
                }

                if (bindItemLayoutId == 0) {
                    if (popupEntity.isDarkTheme) {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(getColor(getContext(), ResourceTable.Color__xpopup_white_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(getShapeElement(getColor(getContext(), ResourceTable.Color__xpopup_list_dark_divider)));
                    } else {
                        holder.<Text>getView(ResourceTable.Id_tv_text).setTextColor(new Color(getColor(getContext(), ResourceTable.Color__xpopup_text_dark_color)));
                        holder.getView(ResourceTable.Id_item_dirver).setBackground(getShapeElement(getColor(getContext(), ResourceTable.Color__xpopup_list_divider)));
                    }
                    if (position == data.length - 1) {
                        holder.getView(ResourceTable.Id_item_dirver).setVisibility(HIDE);
                    } else {
                        holder.getView(ResourceTable.Id_item_dirver).setVisibility(VISIBLE);
                    }
                }
            }
        };
        listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getData().get(position));
                }
                if (popupEntity.autoDismiss) {
                    dismiss();
                }
            }
        });
        listContainer.setItemProvider(adapter);
    }


    String[] data;
    int[] iconIds;

    public PopupWindowView setStringData(String[] data, int[] iconIds) {
        this.data = data != null ? data.clone() : data;
        this.iconIds = iconIds != null ? iconIds.clone() : iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public PopupWindowView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    public interface OnSelectListener {
        void onSelect(int position, String text);
    }

//    ---------------------------------------------------------------

    /**
     * 执行初始化Popup
     */
    protected void init() {
        initPopupContent();
        if (!isCreated) {
            isCreated = true;
            onCreate();
        }
        handler.postTask(initTask, 10);
    }


    private void detachFromHost() {
        if (dialog != null) {
            dialog.destroy();
        }
    }

    protected void initAnimator() {
        // 优先使用自定义的动画器
        if (popupEntity.customAnimator != null) {
            popupContentAnimator = popupEntity.customAnimator;
            popupContentAnimator.targetView = getPopupContentView();
        } else {
            // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
            popupContentAnimator = genAnimatorByPopupType();
            if (popupContentAnimator == null) {
                popupContentAnimator = getPopupAnimator();
            }
        }

        if (popupContentAnimator != null) {
            popupContentAnimator.initAnimator();
        }
    }

    public PopupWindowView show() {
        if (popupStatus == PopupStatus.Showing) {
            return this;
        }
        popupStatus = PopupStatus.Showing;
        if (dialog != null && dialog.isShowing()) {
            return PopupWindowView.this;
        }
        handler.postTask(attachTask);
        return this;
    }

    public MyPopup dialog;

    private void attachDialog() {
        if (dialog == null) {
            dialog = new MyPopup(getContext())
                    .setContent(PopupWindowView.this);
        }
        if (getContext() instanceof Ability) {
            ((Ability) getContext()).getLifecycle().addObserver(this);
        }

        dialog.show();
        if (popupEntity == null) {
            throw new IllegalArgumentException("如果弹窗对象是复用的，则不要设置isDestroyOnDismiss(true)");
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
        }
    };


    public void focusAndProcessBackPress() {
        if (popupEntity != null && popupEntity.isRequestFocus) {
            setTouchFocusable(true);
            requestFocus();
            // 此处焦点可能被内部的EditText抢走，也需要给EditText也设置返回按下监听

            setKeyEventListener(new BackPressListener());
//            if (!popupEntity.autoFocusEditText) {
//            }

            // let all EditText can process back pressed.
            ArrayList<TextField> list = new ArrayList<>();
            PopupUtils.findAllEditText(list, (ComponentContainer) getPopupContentView());
            for (int i = 0; i < list.size(); i++) {
                final TextField et = list.get(i);
                et.setKeyEventListener(new BackPressListener());
                if (i == 0 && popupEntity.autoFocusEditText) {
                    et.setFocusable(FOCUS_ENABLE);
                    et.setTouchFocusable(true);
                    et.requestFocus();
                }
            }
        }
    }


    class BackPressListener implements KeyEventListener {
        @Override
        public boolean onKeyEvent(Component component, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEY_BACK && popupEntity != null) {
                return true;
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
        if (popupEntity == null || popupEntity.popupAnimation == null) {
            return null;
        }
        return null;
    }


    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        getPopupContentView().setAlpha(1f);
        setVisibility(VISIBLE);

        if (popupContentAnimator != null) {
            popupContentAnimator.animateShow();
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
        return ((ComponentContainer) getPopupContentView()).getComponentAt(0);
    }

    public int getAnimationDuration() {
        return popupEntity.popupAnimation == PopupAnimation.NoAnimation ? 10 : 360;
    }

    /**
     * 弹窗的最大宽度，用来限制弹窗的最大宽度
     * 返回0表示不限制，默认为0
     *
     * @return 弹窗的最大宽度
     */
    protected int getMaxWidth() {
        return popupEntity.maxWidth;
    }

    /**
     * 弹窗的最大高度，用来限制弹窗的最大高度
     * 返回0表示不限制，默认为0
     *
     * @return 弹窗的最大高度
     */
    protected int getMaxHeight() {
        return popupEntity.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     * 返回0表示不设置，默认为0
     *
     * @return 弹窗的宽度
     */
    protected int getPopupWidth() {
        return popupEntity.popupWidth;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     * 返回0表示不设置，默认为0
     *
     * @return 弹窗的高度
     */
    protected int getPopupHeight() {
        return popupEntity.popupHeight;
    }

    public void dismiss() {
        handler.removeTask(attachTask);
        handler.removeTask(initTask);
        if (popupStatus == PopupStatus.Dismissing || popupStatus == PopupStatus.Dismiss) {
            return;
        }
        popupStatus = PopupStatus.Dismissing;
        clearFocus();
        if (popupContentAnimator != null) {
            popupContentAnimator.animateDismiss();
        }
        handler.removeTask(doAfterDismissTask);
        handler.postTask(doAfterDismissTask, getAnimationDuration());
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


    Runnable dismissWithRunnable;

    @Override
    public void onStateChanged(Lifecycle.Event event, Intent intent) {
        onDestroy();
    }

    public void onDestroy() {
        destroy();
    }

    public void destroy() {
        detachFromHost();
        onComponentUnboundFromWindow(null);
        if (popupEntity != null) {
            popupEntity.atView = null;
            popupEntity.watchView = null;
            popupEntity.decorView = null;
            dialog = null;
            if (popupEntity.isDestroyOnDismiss) {
                popupEntity = null;
            }
        }
        if (getContext() instanceof Ability) {
            ((Ability) getContext()).getLifecycle().removeObserver(this);
        }
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        handler.removeAllEvent();
        //// 如果开启isDestroyOnDismiss，强制释放资源
        if (popupEntity != null && popupEntity.isDestroyOnDismiss) {
            popupEntity.atView = null;
            popupEntity.watchView = null;
            popupEntity.decorView = null;
            popupEntity = null;
            dialog = null;
            if (getContext() instanceof Ability) {
                ((Ability) getContext()).getLifecycle().removeObserver(this);
            }
        }
        popupStatus = PopupStatus.Dismiss;
    }

    @Override
    public void onComponentBoundToWindow(Component component) {

    }

    private float x;
    private float y;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        getGlobalVisibleRect(getPopupImplView(), rect);

        if (!PopupUtils.isInRect(EventUtil.getX(event), EventUtil.getY(event), rect)) {
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    x = EventUtil.getRawX(event);
                    y = EventUtil.getRawY(event);
                    if (dialog != null && popupEntity != null && popupEntity.isClickThrough) {
                        dialog.passClick(event);
                    }
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                case TouchEvent.CANCEL:
                    float dx = EventUtil.getRawX(event) - x;
                    float dy = EventUtil.getRawY(event) - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && popupEntity != null && popupEntity.isDismissOnTouchOutside) {
                        dismiss();
                        getGlobalVisibleRect(getPopupImplView(), rect2);
                        if (!PopupUtils.isInRect(EventUtil.getX(event), EventUtil.getY(event), rect2)) {
                            if (dialog != null && popupEntity != null && popupEntity.isClickThrough) {
                                dialog.passClick(event);
                            }
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
     * 获取控件的显示区域
     *
     * @param component 控件
     * @param rect      用于存储显示区域的Rect
     * @return 存储了控件显示区域的Rect
     */
    private Rect getGlobalVisibleRect(Component component, Rect rect) {
        int[] locationOnScreen = component.getLocationOnScreen();
        rect.left = locationOnScreen[0];
        rect.right = rect.left + component.getWidth();
        rect.top = locationOnScreen[1];
        rect.bottom = rect.top + component.getHeight();
        return rect;
    }

    //    ----------------------------------------------------------------------------------
    public boolean isShowUp;
    public boolean isShowLeft;
    protected int bgDrawableMargin = 6;
    protected int defaultOffsetY = 0;
    protected int defaultOffsetX = 0;
    protected PartShadowContainer attachPopupContainer;

    protected void addInnerContent() {
        Component contentView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.removeAllComponents();
        attachPopupContainer.addComponent(contentView);
    }

    private void initPopupContent() {
        if (attachPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        if (popupEntity.getAtView() == null && popupEntity.touchPoint == null) {
            throw new IllegalArgumentException("atView() or watchView() must be call for AttachPopupView before show()！");
        }

        defaultOffsetY = popupEntity.offsetY == 0 ? PopupUtils.vp2px(getContext(), 4) : popupEntity.offsetY;
        defaultOffsetX = popupEntity.offsetX;

        attachPopupContainer.setTranslationX(popupEntity.offsetX);
        attachPopupContainer.setTranslationY(popupEntity.offsetY);
        applyBg();
        PopupUtils.applyPopupSize((ComponentContainer) getPopupContentView(), getMaxWidth(), getMaxHeight(), getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    protected void applyBg() {
        if (!isCreated) {
            // 优先使用implView的背景
            if (getPopupImplView().getBackgroundElement() == null) {
                defaultOffsetX -= bgDrawableMargin;
                defaultOffsetY -= bgDrawableMargin;
                attachPopupContainer.setBackground(PopupUtils.createDrawable((popupEntity.isDarkTheme ? getColor(getContext(), ResourceTable.Color__xpopup_bg_dark_color)
                        : getColor(getContext(), ResourceTable.Color__xpopup_light_color)), popupEntity.borderRadius));
            } else {
                Element backgroundElement = getPopupImplView().getBackgroundElement();
                if (backgroundElement != null) {
                    attachPopupContainer.setBackground(backgroundElement);
                    getPopupImplView().setBackground(null);
                }
            }
        }
    }

    public float translationX = 0;
    public float translationY = 0;

    // 弹窗显示的位置不能超越Window高度
    float maxY = PopupUtils.getScreenHeight(getContext());
    int overflow = PopupUtils.vp2px(getContext(), 10);
    float centerY = 0;

    public void doAttach() {
        maxY = PopupUtils.getScreenHeight(getContext()) - overflow;
        final boolean isRTL = false;
        // 0. 判断是依附于某个点还是某个View
        if (popupEntity.touchPoint != null) {
//            if (XPopup.longClickPoint != null) {
//                popupEntity.touchPoint = XPopup.longClickPoint;
//            }
            centerY = popupEntity.touchPoint.getY();
            // 依附于指定点,尽量优先放在下方，当不够的时候在显示在上方
            isShowUp = false;
            if ((popupEntity.touchPoint.getY() + getPopupContentView().getHeight()) > maxY - getStatusBarHeight()) { // 如果下方放不下，超出window高度
                if (popupEntity.touchPoint.getY() > PopupUtils.getScreenHeight(getContext()) / 2) { // 如果触摸点在屏幕中心的下方
                    isShowUp = true;
                }
            }
            isShowLeft = popupEntity.touchPoint.getX() < PopupUtils.getWindowWidth(getContext()) / 2;

            // 限制最大宽高
            ComponentContainer.LayoutConfig params = getPopupContentView().getLayoutConfig();
            int maxHeight = (int) (isShowUpToTarget() ? (popupEntity.touchPoint.getY() - getStatusBarHeight() - overflow)
                    : (PopupUtils.getScreenHeight(getContext()) - popupEntity.touchPoint.getY() - overflow));
            int maxWidth = (int) (isShowLeft ? (PopupUtils.getWindowWidth(getContext()) - popupEntity.touchPoint.getX() - overflow) : (popupEntity.touchPoint.getX() - overflow));
            if (getPopupContentView().getHeight() > maxHeight) {
                params.height = maxHeight;
            }
            if (getPopupContentView().getHeight() > maxWidth) {
                params.width = maxWidth;
            }
            getPopupContentView().setLayoutConfig(params);

            if (isRTL) {
                translationX = isShowLeft ? -(PopupUtils.getWindowWidth(getContext()) - popupEntity.touchPoint.getX() - getPopupContentView().getWidth() - defaultOffsetX)
                        : -(PopupUtils.getWindowWidth(getContext()) - popupEntity.touchPoint.getX() + defaultOffsetX);
            } else {
                translationX = isShowLeft ? (popupEntity.touchPoint.getX() + defaultOffsetX) : (popupEntity.touchPoint.getX() - getPopupContentView().getWidth() - defaultOffsetX);
            }
            if (popupEntity.isCenterHorizontal) {
                // 水平居中
                if (isShowLeft) {
                    if (isRTL) {
                        translationX += getPopupContentView().getHeight() / 2f;
                    } else {
                        translationX -= getPopupContentView().getHeight() / 2f;
                    }
                } else {
                    if (isRTL) {
                        translationX -= getPopupContentView().getWidth() / 2f;
                    } else {
                        translationX += getPopupContentView().getWidth() / 2f;
                    }
                }
            }
            if (isShowUpToTarget()) {
                // 应显示在point上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationY = popupEntity.touchPoint.getY() - getPopupContentView().getHeight() - defaultOffsetY;
            } else {
                translationY = popupEntity.touchPoint.getY() + defaultOffsetY;
            }
        } else {
            // 依附于指定View
            // 1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            int[] rectOld = popupEntity.getAtView().getLocationOnScreen();
            locations[0] = rectOld[0];
            locations[1] = rectOld[1];
            final Rect rect = new Rect(locations[0], locations[1], locations[0] + popupEntity.getAtView().getWidth(),
                    locations[1] + popupEntity.getAtView().getHeight());
            final int centerX = (rect.left + rect.right) / 2;

            // 尽量优先放在下方，当不够的时候在显示在上方
            centerY = (rect.top + rect.bottom) / 2;
            isShowUp = false;
            if ((rect.bottom + getPopupContentView().getHeight()) > maxY + getStatusBarHeight()) { // 如果下方放不下，超出window高度
                if (centerY > PopupUtils.getScreenHeight(getContext()) / 2) { // 如果触摸点在屏幕中心的下方
                    isShowUp = true;
                }
            }
            isShowLeft = centerX < PopupUtils.getWindowWidth(getContext()) / 2;
            // 修正高度，弹窗的高有可能超出window区域
            if (!isCreated) {
                ComponentContainer.LayoutConfig params = getPopupContentView().getLayoutConfig();
                int maxHeight = isShowUpToTarget() ? (rect.top - getStatusBarHeight() - overflow) : (PopupUtils.getScreenHeight(getContext()) - rect.bottom - overflow);
                int maxWidth = isShowLeft ? (PopupUtils.getWindowWidth(getContext()) - rect.left - overflow) : (rect.right - overflow);
                if (getPopupContentView().getHeight() > maxHeight) {
                    params.height = maxHeight;
                }
                if (getPopupContentView().getWidth() > maxWidth) {
                    params.width = maxWidth;
                }
                getPopupContentView().setLayoutConfig(params);
            }
            if (isRTL) {
                translationX = isShowLeft ? -(PopupUtils.getWindowWidth(getContext()) - rect.left - getPopupContentView().getWidth() - defaultOffsetX) : -(PopupUtils.getWindowWidth(getContext()) - rect.right + defaultOffsetX);
            } else {
                translationX = isShowLeft ? (rect.left + defaultOffsetX) : (rect.right - getPopupContentView().getWidth() - defaultOffsetX);
            }
            if (popupEntity.isCenterHorizontal) {
                // 水平居中
                float v = (rect.getWidth() - getPopupContentView().getWidth()) / 2f;
                if (isShowLeft) {
                    if (isRTL) {
                        translationX -= v;
                    } else {
                        translationX += v;
                    }
                } else {
                    if (isRTL) {
                        translationX += v;
                    } else {
                        translationX -= v;
                    }
                }
            }
            if (isShowUpToTarget()) {
                // 说明上面的空间比较大，应显示在atView上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationY = rect.top - getPopupContentView().getHeight() - defaultOffsetY;
            } else {
                translationY = rect.bottom + defaultOffsetY;
            }
        }
        translationY -= getStatusBarHeight(); // 减去状态栏的高度
        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    public int getStatusBarHeight() {
        return 130;
    }

    // 是否显示在目标上方
    protected boolean isShowUpToTarget() {
        if (popupEntity.positionByWindowCenter) {
            // 目标在屏幕上半方，弹窗显示在下；反之，则在上
            return centerY > PopupUtils.getScreenHeight(getContext()) / 2;
        }
        // 默认是根据Material规范定位，优先显示在目标下方，下方距离不足才显示在上方
        return (isShowUp);
    }

    protected PopupAnimator getPopupAnimator() {
        if (isShowUpToTarget()) {  // 在上方展示
            return new ScrollScaleAnimator(getPopupContentView(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftBottom
                    : PopupAnimation.ScrollAlphaFromRightBottom);
        } else {  // 在下方展示
            return new ScrollScaleAnimator(getPopupContentView(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftTop
                    : PopupAnimation.ScrollAlphaFromRightTop);
        }
    }

    public enum PopupStatus {
        Show, // 显示
        Showing,  // 正在执行显示动画
        Dismiss,  // 隐藏
        Dismissing // 正在执行消失动画
    }

    public static ShapeElement getShapeElement(int bgColor) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(bgColor));
        return shapeElement;
    }

    public static int getColor(Context context, int resColorId) {
        try {
            return context.getResourceManager().getElement(resColorId).getColor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0x000000;
    }

    private final Runnable attachTask = () -> {
        init();
        attachDialog();
    };
    private final Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            if (popupEntity == null) {
                return;
            }

            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null; // no cache, avoid some bad edge effect.
            }
            popupStatus = PopupStatus.Dismiss;

//            if (popupEntity.isRequestFocus && popupEntity.decorView != null) {
//                // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
////                    LogUtil.debug("XPopup", "waiting for improvement");
//            }

            // 移除弹窗，GameOver
            detachFromHost();
        }
    };

    private final Runnable initTask = new Runnable() {
        @Override
        public void run() {
            if (dialog == null) {
                return;
            }
            focusAndProcessBackPress();
            // 由于Attach弹窗有个位置设置过程，需要在位置设置完毕自己开启动画
            // 2. 收集动画执行器
            initAnimator();
            doShowAnimation();
            doAfterShow();
        }
    };

}
