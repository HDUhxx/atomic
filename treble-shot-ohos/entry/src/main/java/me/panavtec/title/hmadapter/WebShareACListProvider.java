package me.panavtec.title.hmadapter;

import me.panavtec.title.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;

import static me.panavtec.config.AppConfig.SERVER_PORT_WEBSHARE;

public class WebShareACListProvider extends BaseItemProvider {
    private List<ACInfo> mFilteredList;
    private List<ACInfo> mList;
    private Context mContext;

    public void setList(List<ACInfo> list) {mList = list;}
    public WebShareACListProvider(List<ACInfo> list, Context context) {
        if(list == null) {
            list = new ArrayList<ACInfo>() ;
            list.add(new ACInfo("Wi-fi","10.214.21.45"));
            list.add(new ACInfo("Radio0","10.214.21.46"));
        }
        this.mList = list;
        this.mFilteredList = list;
        this.setContext(context);
    }

    public void filteredByTitle(String title){
        mFilteredList = new ArrayList<>();
        mList.forEach(acInfo -> {
            if(acInfo.mTitle.contains(title)){
                mFilteredList.add(acInfo);
            }
        });
    }

    @Override
    public int getCount() {
        return getFilteredList() == null ? 0 : getFilteredList().size();
    }

    @Override
    public Object getItem(int position) {
        if (getFilteredList() != null && position >= 0 && position < getFilteredList().size()) {
            return getFilteredList().get(position);
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
            cpt = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_list_active_connection, null, false);
            viewHolder = new ViewHolder(cpt);
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (ViewHolder) cpt.getTag();
        }
        viewHolder.titleCtrl.setText(getFilteredList().get(position).getTitle());
        viewHolder.iPCtrl.setText(getFilteredList().get(position).getIP());
        return cpt;
    }

    public List<ACInfo> getFilteredList() {
        return mFilteredList;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public static class ACInfo{
        private String mTitle;
        private String mIP;

        public ACInfo(String title, String ip) {
            setTitle(title);
            setIP(ip);
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public String getIP() {
            return mIP;
        }

        public void setIP(String iP) {
            this.mIP = "http://" + iP + ":" + SERVER_PORT_WEBSHARE + "/";
        }
    }

    static class ViewHolder {
        final Text titleCtrl;
        final Text iPCtrl;
        ViewHolder(Component component) {
            titleCtrl = (Text) component.findComponentById(ResourceTable.Id_ac_text);
            iPCtrl = (Text) component.findComponentById(ResourceTable.Id_ac_text1);

            component.findComponentById(ResourceTable.Id_ac_visitView)
            .setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Intent intent = new Intent().setUri(Uri.parse(iPCtrl.getText()));
                    component.getContext().startAbility(intent,0);
                }
            });
        }
    }
}

