/*
 *    Copyright 2021 youth5201314
 *    Copyright 2021 Institute of Software Chinese Academy of Sciences, ISRC

 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youth.banner;


import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;
import com.youth.banner.view.BannerViewPager;
import ohos.agp.components.*;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.multimodalinput.event.TouchEvent.*;


public class Banner extends StackLayout implements PageSlider.PageChangedListener {
    public String tag = "banner";
    private int mIndicatorMargin = BannerConfig.PADDING_SIZE;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorSelectedWidth;
    private int mIndicatorSelectedHeight;
    private int indicatorSize;
    private int bannerBackgroundImage;
    private int bannerStyle = BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE;
    private int delayTime = BannerConfig.TIME;
    private int scrollTime = BannerConfig.DURATION;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = BannerConfig.IS_SCROLL;
    private int mIndicatorSelectedResId = ResourceTable.Graphic_gray_radius;
    private int mIndicatorUnselectedResId = ResourceTable.Graphic_white_radius;
    private int mLayoutResId = ResourceTable.Layout_banner;
    private int titleHeight;
    private int titleBackground;
    private int titleTextColor;
    private int titleTextSize;
    private int startIndex;
    private int count = 0;
    private int currentItem;
    private int gravity = -1;
    private int lastPosition = 1;
    private Image.ScaleMode scaleMode = Image.ScaleMode.ZOOM_CENTER;
    private int scaleType = 3;
    private List<String> titles;
    private List imageUrls;
    private List<Component> imageViews;
    private List<Component> indicatorImages;
    private Context context;
    private BannerViewPager viewPager;
    private Text bannerTitle, numIndicatorInside, numIndicator;
    private DirectionalLayout indicator, indicatorInside, titleView;
    private Image bannerDefaultImage;
    private ImageLoaderInterface imageLoader;
    private BannerPagerAdapter adapter;
    private PageSlider.PageChangedListener mOnPageChangeListener;
    private BannerScroller mScroller;
    private OnBannerClickListener bannerListener;
    private OnBannerListener listener;
    private DisplayManager dm;
    private HiLogLabel logLabel = new HiLogLabel(HiLog.LOG_APP,0,tag);
    EventRunner runner = EventRunner.current();
    private WeakHandler handler = new WeakHandler(runner);

    public Banner(Context context) throws NotExistException, WrongTypeException, IOException {
        this(context, null);
    }

    public Banner(Context context, AttrSet attrs) throws NotExistException, WrongTypeException, IOException {
        this(context, attrs, null);
    }

    public Banner(Context context, AttrSet attrs, String defStyle) throws NotExistException, WrongTypeException, IOException {
        super(context, attrs, defStyle);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        dm = DisplayManager.getInstance();

        indicatorSize = dm.getDefaultDisplay(context).get().getAttributes().width/80;
        HiLog.fatal(logLabel, String.valueOf(indicatorSize));

        initView(context, attrs);
    }

    private void initView(Context context, AttrSet attrs) throws NotExistException, WrongTypeException, IOException {
        imageViews.clear();
        handleTypedArray(context,attrs);
        Component component = LayoutScatter.getInstance(context).parse(mLayoutResId,this,true);

        bannerDefaultImage = (Image)component.findComponentById(ResourceTable.Id_bannerDefaultImage);
        viewPager = (BannerViewPager) component.findComponentById(ResourceTable.Id_bannerViewPager);
        titleView = (DirectionalLayout) component.findComponentById(ResourceTable.Id_titleView);
        indicator = (DirectionalLayout) component.findComponentById(ResourceTable.Id_circleIndicator);
        indicatorInside = (DirectionalLayout) component.findComponentById(ResourceTable.Id_indicatorInside);
        bannerTitle = (Text) component.findComponentById(ResourceTable.Id_bannerTitle);
        numIndicator = (Text)component.findComponentById(ResourceTable.Id_numIndicator);
        numIndicatorInside = (Text) component.findComponentById(ResourceTable.Id_numIndicatorInside);
        bannerDefaultImage.setImageAndDecodeBounds(bannerBackgroundImage);
        initViewPagerScroll();
    }

    private void handleTypedArray(Context context, AttrSet attrs) throws NotExistException, WrongTypeException, IOException {

        ResourceManager r = context.getResourceManager();
//        TypedAttribute typedArray = r.getSolidXml(0).getRoot().getTypedAttributes(r)
        mIndicatorWidth =indicatorSize;
        mIndicatorHeight = indicatorSize;
        mIndicatorSelectedWidth = indicatorSize;
        mIndicatorSelectedHeight = indicatorSize;
        mIndicatorMargin = BannerConfig.PADDING_SIZE;
        mIndicatorSelectedResId = ResourceTable.Graphic_gray_radius;
        mIndicatorUnselectedResId =  ResourceTable.Graphic_white_radius;

        delayTime =  BannerConfig.TIME;
        scrollTime =  BannerConfig.DURATION;
        isAutoPlay = BannerConfig.IS_AUTO_PLAY;

        titleBackground =  BannerConfig.TITLE_BACKGROUND;
        titleHeight = BannerConfig.TITLE_HEIGHT;
        titleTextColor =  BannerConfig.TITLE_TEXT_COLOR;
        titleTextSize =  BannerConfig.TITLE_TEXT_SIZE;
        mLayoutResId =  mLayoutResId;
        bannerBackgroundImage = ResourceTable.Media_no_banner;
    }

    private void initViewPagerScroll() {
        try {
            Field mField = PageSlider.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            HiLog.error(logLabel, e.getMessage());
        }
    }

    public Banner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public Banner setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public Banner setBannerBackgroundImage(int attr){
        this.bannerBackgroundImage = attr;
        return this;
    }

    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public Banner setTitleHeight(int attr){
        this.titleHeight = attr;
        return this;
    }

    public Banner setTitleTextColor(int attr){
        this.titleTextColor = attr;
        return this;
    }

    public Banner setTitleTextSize(int attr){
        this.titleTextSize = attr;

        return this;
    }

    public Banner setTitleBackground(int attr){
        this.titleBackground = attr;
        return this;
    }

    public Banner setMIndicatorWidth(int attr){
        this.mIndicatorWidth = attr;
        return this;
    }

    public Banner setMIndicatorHeight(int attr){
        this.mIndicatorHeight = attr;
        return this;
    }

    public Banner setMIndicatorSelectedWidth(int attr){
        this.mIndicatorSelectedWidth = attr;
        return this;
    }

    public Banner setMIndicatorSelectedHeight(int attr){
        this.mIndicatorSelectedHeight = attr;
        return this;
    }

    public Banner setMIndicatorSelectedResId(int attr){
        this.mIndicatorSelectedResId = attr;
        return this;
    }

    public Banner setMIndicatorUnselectedResId(int attr){
        this.mIndicatorUnselectedResId = attr;
        return this;
    }

    public Banner setMIndicatorMargin(int attr){
        this.mIndicatorMargin = attr;
        return this;
    }

    public Banner setScaleType(Image.ScaleMode mode){
        scaleMode = mode;
        return this;
    }

    public Banner setScaleType(int scale){
        scaleType = scale;
//        this.invalidate();
        return this;
    }

    public Banner setIndicatorGravity(int type) {
        switch (type) {
            case BannerConfig.LEFT:
                this.gravity = LayoutAlignment.LEFT |  LayoutAlignment.VERTICAL_CENTER;
                break;
            case BannerConfig.CENTER:
                this.gravity =  LayoutAlignment.CENTER;
                break;
            case BannerConfig.RIGHT:
                this.gravity =  LayoutAlignment.RIGHT |  LayoutAlignment.VERTICAL_CENTER;
                break;
        }
        return this;
    }

    public Banner setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public Banner setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public Banner setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public Banner setImages(List<?> imageUrls) {
        this.imageUrls.addAll(imageUrls);
        this.count = imageUrls.size();
        return this;
    }

    public void update(List<?> imageUrls, List<String> titles) throws NotExistException, WrongTypeException, IOException {
        this.titles.clear();
        this.titles.addAll(titles);
        this.update(imageUrls);
    }

    public void update(List<?> imageUrls) throws NotExistException, WrongTypeException, IOException {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        this.start();
    }

    public void updateBannerStyle(int bannerStyle) throws NotExistException, WrongTypeException, IOException {
        this.indicator.setVisibility(HIDE);
        this.numIndicator.setVisibility(HIDE);
        this.numIndicatorInside.setVisibility(HIDE);
        this.indicatorInside.setVisibility(HIDE);
        this.bannerTitle.setVisibility(HIDE);
        this.titleView.setVisibility(HIDE);
        this.bannerStyle = bannerStyle;
        this.start();
    }

    public Banner start() throws NotExistException, WrongTypeException, IOException {
        this.setBannerStyleUI();
        this.setImageList(this.imageUrls);
        this.setData();
        return this;
    }

    private void setTitleStyleUI() {
        if (this.titles.size() != imageUrls.size()) {
            throw new RuntimeException("[Banner] --> The number of titles and images is different");
        }
        else{
            if (this.titleBackground != -1) {
                this.titleView.setBackground(new ElementScatter(context).parse(titleBackground));
            }
            if (this.titleHeight != -1) {
                this.titleView.setLayoutConfig(new DependentLayout.LayoutConfig(MATCH_PARENT, titleHeight));
            }
            if (this.titleTextColor != -1) {
                this.bannerTitle.setTextColor(new Color(titleTextColor));
            }
            if (this.titleTextSize != -1) {
                this.bannerTitle.setTextSize(this.titleTextSize);
            }
            if (this.titles != null && this.titles.size() > 0) {
                this.bannerTitle.setText(titles.get(0));
                this.bannerTitle.setVisibility(VISIBLE);
                this. titleView.setVisibility(VISIBLE);
            }
        }
    }

    private void setBannerStyleUI() {
        int visibility = this.count > 1 ? VISIBLE : HIDE;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                this.indicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR:
                this.numIndicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                this.numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                this.indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                this.indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
        }
    }

    private void initImages() {
        this.imageViews.clear();
        if (this.bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                this.bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                this.bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR_TITLE) {
            this.numIndicatorInside.setText("1/" + count);
            this.numIndicator.setVisibility(INVISIBLE);
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {
            this.numIndicator.setText("1/" + count);
            this.numIndicatorInside.setVisibility(INVISIBLE);
        }
    }

    private void setImageList(List<?> imagesUrl) throws NotExistException, WrongTypeException, IOException {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            this.bannerDefaultImage.setVisibility(VISIBLE);
            HiLog.error(logLabel, "The image data set is empty.");
            return;
        }
        this.bannerDefaultImage.setVisibility(HIDE);
        initImages();

        for (int i = 0; i <= count + 1; i++) {
            Image imageView = null;
            if (this.imageLoader != null) {
                imageView = (Image) this.imageLoader.createImageView(this.context);
            }
            if (imageView == null) {
                imageView = new Image(context);
            }


            Object url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }

            this.imageViews.add(imageView);
            if (this.imageLoader != null){
                this.imageLoader.displayImage(this.context, url, (Component) imageView);
            } else {
                HiLog.error(logLabel, "Please set images loader.");
            }

            imageView.setScaleMode(scaleMode);
//            Object url = null;
//            if (i == 0) {
//                url = imagesUrl.get(count - 1);
//            } else if (i == count + 1) {
//                url = imagesUrl.get(0);
//            } else {
//                url = imagesUrl.get(i - 1);
//            }
//
//            Image imageView = new Image(context);
//            imageView.setPixelMap((Integer) url);
//            imageView.setScaleMode(scaleMode);
//            imageView.setLayoutConfig(new DirectionalLayout.LayoutConfig(MATCH_PARENT,MATCH_PARENT, LayoutAlignment.CENTER,0.5f));
//            imageViews.add(imageView);
        }
    }

    private void createIndicator() {
        this.indicatorImages.clear();
        this.indicator.removeAllComponents();
        this.indicatorInside.removeAllComponents();
        numIndicator.setVisibility(HIDE);
        numIndicatorInside.setVisibility(HIDE);

        for (int i = 0; i < count; i++) {
            Image imageView = new Image(context);
            imageView.setScaleMode(Image.ScaleMode.CLIP_CENTER);
            DirectionalLayout.LayoutConfig params = new DirectionalLayout.LayoutConfig(this.mIndicatorWidth, this.mIndicatorHeight);
            params.setMarginLeft(this.mIndicatorMargin);
            params.setMarginRight(this.mIndicatorMargin);

            if (i == 0) {
                imageView.setBackground(ElementScatter.getInstance(getContext()).parse(mIndicatorSelectedResId));
            } else {
                imageView.setBackground(ElementScatter.getInstance(getContext()).parse(mIndicatorUnselectedResId));
            }

            this.indicatorImages.add(imageView);
            if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                    bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE)
                indicator.addComponent(imageView, params);
            else if (bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                indicatorInside.addComponent(imageView, params);
        }
    }

    private void setData() {
        if (startIndex != 0) {
            currentItem = startIndex;
        } else {
            currentItem = 1;
        }
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addPageChangedListener(this);
            viewPager.setProvider(adapter);
        }else {
            adapter.notifyDataChanged();
        }
        viewPager.setFocusable(1);
//        viewPager.setCurrentPage(currentItem,false);
        if (gravity != -1)
            indicator.setAlignment(gravity);
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
        this.setTouchEventListener(new TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                if (isAutoPlay) {
                    int action = touchEvent.getAction();
                    if (action == 	PRIMARY_POINT_UP || action == 	CANCEL
                            || action == NONE) {
                        startAutoPlay();
                    } else if (action ==	PRIMARY_POINT_DOWN) {
                        stopAutoPlay();
                    }
                }
                return true;
            }
        });
    }


    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);

    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentPage(currentItem, true);
                     handler.post(task);
                } else {
                    viewPager.setCurrentPage(currentItem,true);
                    handler.postDelayed(task, delayTime);

                }
            }
        }
    };



    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = 0;
        if(count!=0){
            realPosition = (position - 1) % count;
        }
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    @Override
    public void onPageSliding(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSliding(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSlideStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSlideStateChanged(state);
        }
//        Log.i(tag,"currentItem: "+currentItem);
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    viewPager.setCurrentPage(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentPage(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentPage(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentPage(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    @Override
    public void onPageChosen(int position) {
        currentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageChosen(toRealPosition(position));
        }
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {

            DirectionalLayout.LayoutConfig
                    Selectedparams = new DirectionalLayout.LayoutConfig(mIndicatorSelectedWidth, mIndicatorSelectedHeight);

            Selectedparams.setMargins(mIndicatorMargin,0,mIndicatorMargin,0);
            DirectionalLayout.LayoutConfig
                    Unselectedparams = new DirectionalLayout.LayoutConfig(mIndicatorWidth, mIndicatorHeight);
            Unselectedparams.setMargins(mIndicatorMargin,0,mIndicatorMargin,0);
            indicatorImages.get((lastPosition - 1 + count) % count).setBackground( ElementScatter.getInstance(getContext()).parse(mIndicatorUnselectedResId));
            indicatorImages.get((lastPosition - 1 + count) % count).setLayoutConfig(Unselectedparams);
            indicatorImages.get((position - 1 + count) % count).setBackground( ElementScatter.getInstance(getContext()).parse(mIndicatorSelectedResId));
            indicatorImages.get((position - 1 + count) % count).setLayoutConfig(Selectedparams);
            lastPosition = position;
        }
        if (position == 0) position = count;
        if (position > count) position = 1;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setText(position + "/" + count);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position + "/" + count);
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                bannerTitle.setText(titles.get(position - 1));
                break;
        }

    }

    class BannerPagerAdapter extends PageSliderProvider {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object createPageInContainer(ComponentContainer container, final int position) {
            container.addComponent(imageViews.get(position));
            Component view = imageViews.get(position);
            if (bannerListener != null) {
                view.setClickedListener(v -> {
                    HiLogLabel logLabel = new HiLogLabel(HiLog.ERROR,0,tag);
                    HiLog.error(logLabel, "你正在使用旧版点击事件接口，下标是从1开始，" +
                            "为了体验请更换为setOnBannerListener，下标从0开始计算");
                    bannerListener.OnBannerClick(position);
                });
            }
            if (listener != null) {
                view.setClickedListener(new ClickedListener() {
                    @Override
                    public void onClick(Component v) {
                        listener.OnBannerClick(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
            componentContainer.removeComponent((Component) o);

        }

        @Override
        public boolean isPageMatchToObject(Component component, Object o) {
            return component == o;
        }

    }



    @Deprecated
    public Banner setOnBannerClickListener(OnBannerClickListener listener) {
        this.bannerListener = listener;
        return this;
    }

    /**
     * 废弃了旧版接口，新版的接口下标是从1开始，同时解决下标越界问题
     *
     * @param listener
     * @return
     */
    public Banner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public Banner setStartIndex(int index) {
        this.startIndex = index;
        return this;
    }

    public void setOnPageChangeListener(PageSlider.PageChangedListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }
}
