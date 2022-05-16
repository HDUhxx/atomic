package com.lxj.xpopupdemo;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.util.ElementUtil;
import com.lxj.xpopup.util.LogUtil;
import com.lxj.xpopup.util.XPermission;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.stackLayout.AllAnimatorDemo;
import com.lxj.xpopupdemo.stackLayout.CustomAnimatorDemo;
import com.lxj.xpopupdemo.stackLayout.CustomPopupDemo;
import com.lxj.xpopupdemo.stackLayout.ImageViewerDemo;
import com.lxj.xpopupdemo.stackLayout.ListContainerDemo;
import com.lxj.xpopupdemo.stackLayout.PartShadowDemo;
import com.lxj.xpopupdemo.stackLayout.QuickStartDemo;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.RadioButton;
import ohos.agp.components.RadioContainer;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.KeyEvent;
import ohos.system.version.SystemVersion;

import java.util.ArrayList;

/**
 * 演示页面
 */
public class MainAbility extends Ability {

    private static final String TAG = MainAbility.class.getName();

    ArrayList<Component> pageview;
    RadioContainer radioContainer;
    private PageSlider pageSlider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        // 修改状态栏的颜色
        StatusBarUtils.setStatusBarColor(this, ElementUtil.getColor(this, ResourceTable.Color_colorBar));
        // 暂未实现的效果先移除出去
        ((RadioContainer) findComponentById(ResourceTable.Id_radio_container)).removeComponentById(ResourceTable.Id_rb_jubu);
        QuickStartDemo view0 = new QuickStartDemo(this);
        ComponentContainer view1 = new PartShadowDemo(this);
        ComponentContainer view2 = new ImageViewerDemo(this);
        ComponentContainer view3 = new AllAnimatorDemo(this);
        ComponentContainer view4 = new CustomPopupDemo(this);
        ComponentContainer view5 = new CustomAnimatorDemo(this);
        ComponentContainer view6 = new ListContainerDemo(this);
        // 将commonent装入数组
        pageview = new ArrayList<Component>();
        pageview.add(view0);
        pageview.add(view1);
        pageview.remove(view1);
        pageview.add(view2);
        pageview.add(view3);
        pageview.add(view4);
        pageview.add(view5);
        pageview.add(view6);
        radioContainer = (RadioContainer) findComponentById(ResourceTable.Id_radio_container);
        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_pagerSlider);

        XPopup.setPrimaryColor(getColor(ResourceTable.Color_colorPrimary));
        LoadingPopupView loadingPopupView = new XPopup.Builder(this)
                .isDestroyOnDismiss(true)
                .asLoading("嘻嘻嘻嘻嘻");
        loadingPopupView.show();
        loadingPopupView.delayDismiss(1200);

        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
            @Override
            public void run() {
                String str = "deviceHeight：" + XPopupUtils.getScreenHeight(MainAbility.this)
                        + "  hapHeight：" + XPopupUtils.getAppHeight(MainAbility.this)
                        + "  statusHeight：" + XPopupUtils.getStatusBarHeight(radioContainer)
                        + "  navHeight：" + XPopupUtils.getNavBarHeight(radioContainer)
                        + "  设备SDK版本：" + SystemVersion.getApiVersion();
                LogUtil.debug("XPopup", str);
            }
        }, 100);

        // 数据适配器
        PageSliderProvider mPagerAdapter = new PageSliderProvider() {
            @Override
            // 获取当前窗体界面数
            public int getCount() {
                return pageview.size();
            }

            // 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int position) {
                componentContainer.addComponent(pageview.get(position));
                return pageview.get(position);
            }

            // 是从ViewGroup中移出当前View
            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int position, Object object) {
                componentContainer.removeComponent(pageview.get(position));
            }

            // 断是否由对象生成界面
            @Override
            public boolean isPageMatchToObject(Component component, Object object) {
                return component == object;
            }

        };
        // 绑定适配器
        pageSlider.setProvider(mPagerAdapter);
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
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
        radioContainer.setMarkChangedListener((radioContainer, checkedId) -> pageSlider.setCurrentPage(checkedId));

    }

    @Override
    protected void onStop() {
        super.onStop();
        pageSlider.removeAllComponents();
        pageSlider = null;
        pageview = null;
    }

    // 如需用到保存图片功能，则需要在保存图片的Ability页面像下面这样重写此方法
    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        XPermission.onRequestPermissionsFromUserResult(this, requestCode, permissions, grantResults);
    }

}
