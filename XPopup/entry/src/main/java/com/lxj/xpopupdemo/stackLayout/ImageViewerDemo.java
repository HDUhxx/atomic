package com.lxj.xpopupdemo.stackLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnImageViewerLongPressListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopup.util.LogUtil;
import com.lxj.xpopup.util.ToastUtil;
import com.lxj.xpopupdemo.ResourceTable;
import com.lxj.xpopupdemo.custom.CustomImagePopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.FileConvert;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;

import java.io.File;
import java.util.ArrayList;

/**
 * Description:
 * Create by lxj, at 2019/1/22
 */
public class ImageViewerDemo extends BaseStackLayout {

    private static final String TAG = ImageViewerDemo.class.getName();
    /**
     * 用来存储需要用到的图片链接
     */
    private static ArrayList<String> list = new ArrayList<>();
    private Context context;

    private String url1;
    private String url2;
    private final int color_black = 0x000000ff;

    public ImageViewerDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_image_preview;
    }

    static {
        list.clear();
        list.add(new StringBuilder("htt").append("ps://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3340872573,4245158918&fm=15&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2016797018,1148910201&fm=26&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3105516817,1398974413&fm=26&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1119394600,3640575336&fm=15&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2658826910,3654477545&fm=26&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3340872573,4245158918&fm=15&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2016797018,1148910201&fm=26&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3105516817,1398974413&fm=26&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1119394600,3640575336&fm=15&gp=0.jpg").toString());
        list.add(new StringBuilder("htt").append("ps://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2658826910,3654477545&fm=26&gp=0.jpg").toString());
    }

    ListContainer listContainer;
    Image image1;
    Image image2;
    PageSlider pager;
    Button btn_custom;

    @Override
    public void init(final Component component) {
        this.context = getContext();
        url1 = new StringBuilder("htt").append("ps://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1547319517,3311474777&fm=26&gp=0.jpg").toString();
        url2 = new StringBuilder("htt").append("ps://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=977788682,845118605&fm=26&gp=0.jpg").toString();
        image1 = (Image) component.findComponentById(ResourceTable.Id_image1);
        image2 = (Image) component.findComponentById(ResourceTable.Id_image2);
        pager = (PageSlider) component.findComponentById(ResourceTable.Id_pager);
        btn_custom = (Button) component.findComponentById(ResourceTable.Id_btn_custom);
        listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_listContainer);
        listContainer.setItemProvider(new ImageProvider(context));

        loadImg(image1, url1);
        loadImg(image2, url2);
        image1.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                new XPopup.Builder(getContext())
                        .asImageViewer(image1, url1, true, 0xf1f1f1ff, -1, 0,
                                false, color_black, new ImageLoader(), new OnImageViewerLongPressListener() {
                                    @Override
                                    public void onLongPressed(BasePopupView popupView, int position) {
                                        ToastUtil.showToast(getContext(), "长按了第" + position + "个图片");
                                    }
                                })
                        .show();
            }
        });
        image2.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                new XPopup.Builder(getContext())
                        .asImageViewer(image2, url2, new ImageLoader())
                        .show();
            }
        });
        pager.setPageCacheSize(1);
        pager.setProvider(new ImagePagerAdapter());

        btn_custom.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 自定义的弹窗需要用asCustom来显示，之前的asImageViewer这些方法当然不能用了。
                CustomImagePopup viewerPopup = new CustomImagePopup(getContext());
                // 自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
                viewerPopup.setSingleSrcView(image2, url2);
                viewerPopup.setXPopupImageLoader(new ImageLoader());
                new XPopup.Builder(getContext())
                        .asCustom(viewerPopup)
                        .show();
            }
        });
    }


    private class ImageProvider extends EasyProvider<String> {
        public ImageProvider(Context context) {
            super(context, list, ResourceTable.Layout_adapter_image);
        }

        @Override
        protected void bind(final ViewHolder holder, final String itemData, final int position) {
            final Image imageView = holder.getView(ResourceTable.Id_image);
            // 1. 加载图片
            loadImg(imageView, itemData);

            // 2. 设置点击
            imageView.setClickedListener(new ClickedListener() {
                @Override
                public void onClick(Component component) {
                    new XPopup.Builder(component.getContext()).asImageViewer(imageView, position, list,
                            true, true, -1, -1, -1, true,
                            new RgbColor(32, 36, 46).asRgbaInt(),
                            new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {

                                }
                            }, new ImageLoader(), null)
                            .show();
                }
            });
        }
    }

    class ImagePagerAdapter extends PageSliderProvider {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object createPageInContainer(ComponentContainer componentContainer, int position) {
            final Image imageView = new Image(context);
            imageView.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
            imageView.setScaleMode(Image.ScaleMode.CLIP_CENTER);

            // 1. 加载图片
            loadImg(imageView, list.get(position));
            // 2. 设置点击
            imageView.setClickedListener(new ClickedListener() {
                @Override
                public void onClick(Component component) {
                    new XPopup.Builder(getContext())
                            .asImageViewer(imageView, position, list, true, false, -1, -1, -1, true, color_black, new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(final ImageViewerPopupView popupView, final int position) {
                                    // 1.pager更新当前显示的图片
                                    // 当启用isInfinite时，position会无限增大，需要映射为当前ViewPager中的页
                                    int realPosi = position % list.size();
                                    pager.setCurrentPage(realPosi, false);
                                    // 2.更新弹窗的srcView，注意这里的position是list中的position，上面ViewPager设置了pageLimit数量，
                                    // 保证能拿到child，如果不设置pageLimit，ViewPager默认最多维护3个page，会导致拿不到child

                                }
                            }, new ImageLoader(), null)
                            .show();
                }
            });
            componentContainer.addComponent(imageView);
            return imageView;
        }

        @Override
        public void destroyPageFromContainer(ComponentContainer componentContainer, int position, Object object) {
            componentContainer.removeComponent((Component) object);
        }

        @Override
        public boolean isPageMatchToObject(Component component, Object object) {
            return component == object;
        }
    }

    private class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, String url, Image imageView) {
            // 必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            loadImg(imageView, url);
        }

        @Override
        public File getImageFile(Context context, String url) {
            try {
                // 后续可替换为Glide.with(context).downloadOnly().load(url).submit().get()
                return OkGo.<File>get(url).tag(this).converter(new FileConvert()).adapt().execute().body();
            } catch (Exception e) {
                LogUtil.error(TAG, e.getMessage());
            }
            return null;
        }
    }

    private void loadImg(Image image, String url) {
        context.getUITaskDispatcher().delayDispatch(new Runnable() {
            @Override
            public void run() {
                Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
            }
        }, 50);
    }

}

