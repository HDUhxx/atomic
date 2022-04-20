package me.panavtec.title.hmadapter.about;

import me.panavtec.dialog.ContextMenuDialog;
import me.panavtec.title.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/** @noinspection unchecked*/
public class ThirdPartyLIBListProvider extends BaseItemProvider {
    private List<ModuleItem> mList;
    private Context mContext;

    public void setList(List list){
        mList = list;
    }

    public ThirdPartyLIBListProvider(Context context) {
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
            cpt = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_list_third_party_library, null, false);
            viewHolder = new ViewHolder(cpt, mList.get(position));
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (ViewHolder) cpt.getTag();
        }

        ModuleItem moduleItem = mList.get(position);
        viewHolder.moduleCtrl.setText(moduleItem.moduleName);

        StringBuilder stringBuilder = new StringBuilder();

        if (moduleItem.moduleVersion != null)
            stringBuilder.append(moduleItem.moduleVersion);

        if (moduleItem.licence != null) {
            if (stringBuilder.length() > 0)
                stringBuilder.append(", ");

            stringBuilder.append(moduleItem.licence);
        }

        viewHolder.otherInfoCtrl.setText(stringBuilder.toString());

        return cpt;
    }



    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
    
    
    static class ViewHolder {
        final Text moduleCtrl;
        final Text otherInfoCtrl;

        ViewHolder(Component component, ModuleItem moduleItem) {
            moduleCtrl = (Text) component.findComponentById(ResourceTable.Id_tpLib_txt1);
            otherInfoCtrl = (Text) component.findComponentById(ResourceTable.Id_tpLib_txt2);
            component.findComponentById(ResourceTable.Id_tpLib_menu).setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    ContextMenuDialog contextMenuDialog = new ContextMenuDialog(component.getContext(), moduleItem);
                    contextMenuDialog.show();
                }
            });
        }
    }

    public static class ModuleItem
    {
        public String moduleName;
        public String moduleUrl;
        public String moduleVersion;
        public String licence;
        public String licenceUrl;

        public ModuleItem(JSONObject licenceObject) throws JSONException
        {
            if (licenceObject.has("moduleName"))
                moduleName = licenceObject.getString("moduleName");

            if (licenceObject.has("moduleUrl"))
                moduleUrl = licenceObject.getString("moduleUrl");

            if (licenceObject.has("moduleVersion"))
                moduleVersion = licenceObject.getString("moduleVersion");

            if (licenceObject.has("moduleLicense"))
                licence = licenceObject.getString("moduleLicense");

            if (licenceObject.has("moduleLicenseUrl"))
                licenceUrl = licenceObject.getString("moduleLicenseUrl");
        }
    }
}

