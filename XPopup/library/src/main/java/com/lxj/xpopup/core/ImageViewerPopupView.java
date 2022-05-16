package com.lxj.xpopup.core;

import com.lxj.xpopup.ResourceTable;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.interfaces.OnDragChangeListener;
import com.lxj.xpopup.interfaces.OnImageViewerLongPressListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.PermissionConstants;
import com.lxj.xpopup.util.ToastUtil;
import com.lxj.xpopup.util.XPermission;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BlankView;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.utils.Rect;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;

/**
 * Description: 大图预览的弹窗，使用Transition实现
 * Create by lxj, at 2019/1/22
 */
public class ImageViewerPopupView extends BasePopupView implements OnDragChangeListener, Component.ClickedListener {

    private StackLayout container;
    private StackLayout photoViewContainer;
    private BlankView placeholderView;
    private Text tv_pager_indicator;
    private Text tv_save;
    private PageSlider pager;
    private List<String> urls = new ArrayList<>();
    private XPopupImageLoader imageLoader;
    private OnSrcViewUpdateListener srcViewUpdateListener;
    private int position;
    private Rect rect = null;
    private Image srcView; // 动画起始的View，如果为null，移动和过渡动画效果会没有，只有弹窗的缩放功能
    private Image snapshotView;
    private boolean isShowPlaceholder = true; // 是否显示占位白色，当图片切换为大图时，原来的地方会有一个白色块
    private int placeholderColor = 0xf1f1f1ff; // 占位View的颜色
    private int placeholderStrokeColor = -1; // 占位View的边框色
    private int placeholderRadius = -1; // 占位View的圆角
    private boolean isShowSaveBtn = true; // 是否显示保存按钮
    private boolean isShowIndicator = true; // 是否页码指示器
    private boolean isInfinite = false; // 是否需要无限滚动
    private Component customView;
    private int bgColor = new RgbColor(32, 36, 46).asRgbaInt(); // 弹窗的背景颜色，可以自定义
    protected OnImageViewerLongPressListener longPressListener;

    public ImageViewerPopupView(Context context, PopupInfo popupInfo) {
        super(context, popupInfo);
        container = (StackLayout) findComponentById(ResourceTable.Id_container);
        if (getImplLayoutId() > 0) {
            customView = LayoutScatter.getInstance(getContext()).parse(getImplLayoutId(), container, false);
            customView.setVisibility(INVISIBLE);
            customView.setAlpha(0);
            container.addComponent(customView);
        }
    }

    @Override
    protected int getInnerLayoutId() {
        return ResourceTable.Layout__xpopup_image_viewer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_pager_indicator = (Text) findComponentById(ResourceTable.Id_tv_pager_indicator);
        tv_save = (Text) findComponentById(ResourceTable.Id_tv_save);
        placeholderView = (BlankView) findComponentById(ResourceTable.Id_placeholderView);
        photoViewContainer = (StackLayout) findComponentById(ResourceTable.Id_photoViewContainer);
        pager = (PageSlider) findComponentById(ResourceTable.Id_pager);
        pager.setProvider(new PhotoViewAdapter());
        pager.setCurrentPage(position);
        pager.setVisibility(INVISIBLE);
        addOrUpdateSnapshot();
        pager.addPageChangedListener(onPageChangeListener);
        if (!isShowIndicator) {
            tv_pager_indicator.setVisibility(HIDE);
        }
        if (!isShowSaveBtn) {
            tv_save.setVisibility(HIDE);
        } else {
            tv_save.setClickedListener(this);
        }
    }

    PageSlider.PageChangedListener onPageChangeListener = new PageSlider.PageChangedListener() {
        @Override
        public void onPageSliding(int itemPos, float itemPosOffset, int itemPosOffsetPixels) {

        }

        @Override
        public void onPageSlideStateChanged(int state) {

        }

        @Override
        public void onPageChosen(int itemPos) {
            position = itemPos;
            showPagerIndicator();
            // 更新srcView
            if (srcViewUpdateListener != null) {
                srcViewUpdateListener.onSrcViewUpdate(ImageViewerPopupView.this, itemPos);
            }
        }
    };

    private void setupPlaceholder() {
        placeholderView.setVisibility(isShowPlaceholder ? VISIBLE : INVISIBLE);
        if (isShowPlaceholder) {
            if (placeholderColor != -1) {
                placeholderView.color = placeholderColor;
            }
            if (placeholderRadius != -1) {
                placeholderView.radius = placeholderRadius;
            }
            if (placeholderStrokeColor != -1) {
                placeholderView.strokeColor = placeholderStrokeColor;
            }
            XPopupUtils.setWidthHeight(placeholderView, rect.getWidth(), rect.getHeight());
            placeholderView.setTranslationX(rect.left);
            placeholderView.setTranslationY(rect.top);
            placeholderView.invalidate();
        }
    }

    private void showPagerIndicator() {
        if (urls.size() > 1) {
            int posi = isInfinite ? position % urls.size() : position;
            tv_pager_indicator.setText((posi + 1) + "/" + urls.size());
        }
        if (isShowSaveBtn) {
            tv_save.setVisibility(VISIBLE);
        }
    }

    private void addOrUpdateSnapshot() {
        if (srcView == null) {
            return;
        }
        if (snapshotView == null) {
            snapshotView = new Image(getContext());
            photoViewContainer.addComponent(snapshotView);
            snapshotView.setTranslationX(rect.left);
            snapshotView.setTranslationY(rect.top);
            XPopupUtils.setWidthHeight(snapshotView, rect.getWidth(), rect.getHeight());
        }
        setupPlaceholder();
        if (imageLoader != null) {
            imageLoader.loadImage(position, urls.get(position), snapshotView);
        }
    }

    @Override
    protected void doAfterShow() {
        // do nothing self.
    }

    @Override
    public void doShowAnimation() {
        if (srcView == null) {
            photoViewContainer.setBackground(ElementUtil.getShapeElement(bgColor));
            pager.setVisibility(VISIBLE);
            showPagerIndicator();
            ImageViewerPopupView.super.doAfterShow();
            return;
        }
        if (customView != null) {
            customView.setVisibility(VISIBLE);
        }
        snapshotView.setVisibility(VISIBLE);

        pager.setVisibility(VISIBLE);
        snapshotView.setVisibility(INVISIBLE);
        showPagerIndicator();
        ImageViewerPopupView.super.doAfterShow();
        snapshotView.setTranslationY(0);
        snapshotView.setTranslationX(0);
        XPopupUtils.setWidthHeight(snapshotView, photoViewContainer.getWidth(), photoViewContainer.getHeight());

        // do shadow anim.
        animateShadowBg(bgColor);
        if (customView != null) {
            customView.createAnimatorProperty().alpha(1.0f).setDuration(getAnimationDuration()).start();
        }
        super.doShowAnimation();

    }

    private void animateShadowBg(final int endColor) {
        photoViewContainer.setBackground(ElementUtil.getShapeElement(endColor));
    }

    private void setbg(Component component, int bgColor) {
        component.setBackground(ElementUtil.getShapeElement(bgColor));
    }

    public ImageViewerPopupView setImageUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    public ImageViewerPopupView setSrcViewUpdateListener(OnSrcViewUpdateListener srcViewUpdateListener) {
        this.srcViewUpdateListener = srcViewUpdateListener;
        return this;
    }

    public ImageViewerPopupView setXPopupImageLoader(XPopupImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    /**
     * 是否显示白色占位区块
     *
     * @param isShow 是否显示白色占位区块
     * @return ImageViewerPopupView自身
     */
    public ImageViewerPopupView isShowPlaceholder(boolean isShow) {
        this.isShowPlaceholder = isShow;
        return this;
    }

    /**
     * 是否显示页码指示器
     *
     * @param isShow 是否显示页码指示器
     * @return ImageViewerPopupView自身
     */
    public ImageViewerPopupView isShowIndicator(boolean isShow) {
        this.isShowIndicator = isShow;
        return this;
    }

    /**
     * 是否显示保存按钮
     *
     * @param isShowSaveBtn 是否显示保存按钮
     * @return ImageViewerPopupView自身
     */
    public ImageViewerPopupView isShowSaveButton(boolean isShowSaveBtn) {
        this.isShowSaveBtn = isShowSaveBtn;
        return this;
    }

    /**
     * 是否需要无限滚动
     *
     * @param isInfinite 是否需要无限滚动
     * @return ImageViewerPopupView自身
     */
    public ImageViewerPopupView isInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
        return this;
    }

    public ImageViewerPopupView setPlaceholderColor(int color) {
        this.placeholderColor = color;
        return this;
    }

    public ImageViewerPopupView setPlaceholderRadius(int radius) {
        this.placeholderRadius = radius;
        return this;
    }

    public ImageViewerPopupView setPlaceholderStrokeColor(int strokeColor) {
        this.placeholderStrokeColor = strokeColor;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param bgColor 背景色,rgba
     * @return ImageViewerPopupView
     */
    public ImageViewerPopupView setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public ImageViewerPopupView setLongPressListener(OnImageViewerLongPressListener longPressListener) {
        this.longPressListener = longPressListener;
        return this;
    }

    /**
     * 设置单个使用的源View。单个使用的情况下，无需设置url集合和SrcViewUpdateListener
     *
     * @param srcView 源View
     * @param url     图片地址
     * @return ImageViewerPopupView
     */
    public ImageViewerPopupView setSingleSrcView(Image srcView, String url) {
        if (this.urls == null) {
            urls = new ArrayList<>();
        }
        urls.clear();
        urls.add(url);
        setSrcView(srcView, 0);
        return this;
    }

    public ImageViewerPopupView setSrcView(Image srcView, int position) {
        this.srcView = srcView;
        this.position = position;
        if (srcView != null) {
            int[] locations = this.srcView.getLocationOnScreen();
            if (XPopupUtils.isLayoutRtl(getContext())) {
                int left = -(XPopupUtils.getWindowWidth(getContext()) - locations[0] - srcView.getWidth());
                rect = new Rect(left, locations[1], left + srcView.getWidth(), locations[1] + srcView.getHeight());
            } else {
                rect = new Rect(locations[0], locations[1], locations[0] + srcView.getWidth(), locations[1] + srcView.getHeight());
            }
        }
        return this;
    }

    public void updateSrcView(Image srcView) {
        setSrcView(srcView, position);
        addOrUpdateSnapshot();
    }

    @Override
    public void onRelease() {
        dismiss();
    }

    @Override
    public void onDragChange(int dy, float scale, float fraction) {
        tv_pager_indicator.setAlpha(1 - fraction);
        if (customView != null) {
            customView.setAlpha(1 - fraction);
        }
        if (isShowSaveBtn) {
            tv_save.setAlpha(1 - fraction);
        }
        photoViewContainer.setBackground((ElementUtil.getShapeElement(bgColor)));
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        srcView = null;
        srcViewUpdateListener = null;
    }

    @Override
    public void onClick(Component component) {
        if (component == tv_save) {
            save();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        pager.removePageChangedListener(onPageChangeListener);
        imageLoader = null;
    }

    /**
     * 保存图片到相册，会自动检查是否有保存权限
     */
    protected void save() {
        XPermission.create(getContext(), PermissionConstants.WRITE_USER_STORAGE)
                .callback(new XPermission.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        XPermission.getInstance().releaseConext();
                        if (imageLoader == null) {
                            ToastUtil.showToast(getContext(), "已获取保存权限，请重新进行保存！");
                        } else {
                            XPopupUtils.saveBmpToAlbum(getContext(), imageLoader, urls.get(isInfinite ? position % urls.size() : position));
                        }
                    }

                    @Override
                    public void onDenied() {
                        XPermission.getInstance().releaseConext();
                        ToastUtil.showToast(getContext(), "没有保存权限，保存功能无法使用！");
                    }

                    @Override
                    public void onForbid(List<String> mPermissionsDeniedForever) {
                        ToastUtil.showToast(getContext(), "请跳转到设置页面授权存储权限，否则保存功能无法使用");
                    }
                }).request();
    }

    public class PhotoViewAdapter extends PageSliderProvider {
        @Override
        // 获取当前窗体界面数
        public int getCount() {
            return isInfinite ? Integer.MAX_VALUE / 2 : urls.size();
        }

        // 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        @Override
        public Object createPageInContainer(ComponentContainer componentContainer, int position) {
            final Image photoView = new Image(container.getContext());
            photoView.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
            photoView.setScaleMode(Image.ScaleMode.ZOOM_CENTER);
            // call LoadImageListener
            if (imageLoader != null) {
                imageLoader.loadImage(position, urls.get(isInfinite ? position % urls.size() : position), photoView);
            }
            componentContainer.addComponent(photoView);
            photoView.setClickedListener(new ClickedListener() {
                @Override
                public void onClick(Component component) {
                    dismiss();
                }
            });
            if (longPressListener != null) {
                photoView.setLongClickedListener(new LongClickedListener() {
                    @Override
                    public void onLongClicked(Component component) {
                        longPressListener.onLongPressed(ImageViewerPopupView.this, position);
                    }
                });
            }
            return photoView;
        }

        // 是从ViewGroup中移出当前View
        @Override
        public void destroyPageFromContainer(ComponentContainer componentContainer, int position, Object object) {
            componentContainer.removeComponent((Component) object);
        }

        // 断是否由对象生成界面
        @Override
        public boolean isPageMatchToObject(Component component, Object object) {
            return component == object;
        }

    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
    }

}
