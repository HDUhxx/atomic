package com.lxj.xpopupdemo.custom;

import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopupdemo.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.RadioButton;
import ohos.agp.components.RadioContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

/**
 * Description: 自定义带有ViewPager的Drawer弹窗
 * Create by dance, at 2019/5/5
 */
public class PagerDrawerPopup extends DrawerPopupView {
    public PagerDrawerPopup(Context context) {
        super(context, null);
    }

    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_pager_drawer;
    }

    RadioContainer radioContainer;
    PageSlider pager;
    String[] titles = new String[]{"首页", "娱乐", "汽车", "八卦", "搞笑", "互联网"};

    @Override
    protected void onCreate() {
        super.onCreate();
        radioContainer = (RadioContainer) findComponentById(ResourceTable.Id_radioContainer);
        pager = (PageSlider) findComponentById(ResourceTable.Id_pager);
        pager.setProvider(new PAdapter());
        pager.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int itemPos, float itemPosOffset, int itemPosOffsetPixels) {

            }

            @Override
            public void onPageSlideStateChanged(int state) {

            }

            @Override
            public void onPageChosen(int itemPos) {
                ((RadioButton) radioContainer.getComponentAt(itemPos)).setChecked(true);
            }
        });
        radioContainer.setMarkChangedListener((radioContainer, checkedId) -> pager.setCurrentPage(checkedId));
    }

    class PAdapter extends PageSliderProvider {
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object createPageInContainer(ComponentContainer container, int position) {
            Text textView = new Text(container.getContext());
            textView.setText(titles[position]);
            textView.setTextSize(60);
            textView.setTextAlignment(TextAlignment.CENTER);
            container.addComponent(textView, new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
            return textView;
        }

        @Override
        public void destroyPageFromContainer(ComponentContainer container, int position, Object object) {
            container.removeComponent((Component) object);
        }

        @Override
        public boolean isPageMatchToObject(Component component, Object object) {
            return component == object;
        }
    }
}
