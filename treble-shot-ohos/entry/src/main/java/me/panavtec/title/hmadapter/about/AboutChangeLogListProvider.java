package me.panavtec.title.hmadapter.about;

import me.panavtec.title.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

/** @noinspection unchecked*/
public class AboutChangeLogListProvider extends BaseItemProvider {
    private List<VersionObject> mList;
    private Context mContext;

    public void setList(List list){
        mList = list;
    }

    public AboutChangeLogListProvider(Context context) {
        this.mList = null;
        this.setContext(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        Component cpt;
        ViewHolder viewHolder;
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_list_changelog, null, false);
            viewHolder = new ViewHolder(cpt);
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (ViewHolder) cpt.getTag();
        }
        viewHolder.nameCtrl.setText(mList.get(position).name);
        viewHolder.changesCtrl.setText(mList.get(position).changes);
        return cpt;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
    
    
    static class ViewHolder {
        final Text nameCtrl;
        final Text changesCtrl;
        ViewHolder(Component component) {
            nameCtrl = (Text) component.findComponentById(ResourceTable.Id_about_change_log_text1);
            changesCtrl = (Text) component.findComponentById(ResourceTable.Id_about_change_log_text2);
        }
    }
    
    public static class VersionObject
    {
        final String tag;
        final String name;
        final String changes;

        public VersionObject(String tag, String name, String changes)
        {
            this.tag = tag;
            this.name = name;
            this.changes = changes;
        }
    }
}

