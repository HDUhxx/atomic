package me.panavtec.title.hmadapter;

import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.AbilitySiceDealInterface;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;

import java.util.List;

public class TestPagerProvider extends PageSliderProvider {
    // 数据源，每个页面对应list中的一项
    private final List<Fraction> list;
    final Context context;
    final AbilitySiceDealInterface dealInterface;
    public TestPagerProvider(List<Fraction> list, Context context,AbilitySiceDealInterface abilitySiceDealInterface) {
        this.list = list;
        this.context = context;
        dealInterface = abilitySiceDealInterface;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        Component cpt;
       switch (i){
           case 0:
               cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_transfer_group_list, null, false);
               componentContainer.addComponent(cpt);
               dealInterface.doPageOne(cpt);
               return componentContainer;
           case 1:
               cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_file_explorer, null, false);
               componentContainer.addComponent(cpt);
               dealInterface.doPageTwo(cpt);
               return componentContainer;
           case 2:
               cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_text_stream, null, false);
               componentContainer.addComponent(cpt);
               dealInterface.doPageThree(cpt);
               return componentContainer;
       }
        return null;

    }
    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }
    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
}