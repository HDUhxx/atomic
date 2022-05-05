package com.github.chrisbanes.sample;

import com.github.chrisbanes.photoview.PhotoView;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;

import java.util.List;

public class PageProvider extends PageSliderProvider {

    private List<Integer> list;
    private Context context;
    private PageSlider slider;

    public PageProvider(List<Integer> list, Context context, PageSlider slider){
        this.list = list;
        this.context = context;
        this.slider = slider;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final int data = list.get(i);
        PhotoView view = new PhotoView(context);
        view.setPixelMap(data);

        view.setLayoutConfig(new ComponentContainer.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_PARENT
        ));
        view.setPageSlider(slider);
        componentContainer.addComponent(view);
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
