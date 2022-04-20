package me.panavtec.title.hmadapter;

import com.ohos.trebleshot.config.AppConfig;
import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.exception.NotReadyException;
import com.ohos.trebleshot.object.Editable;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.ConnectionUtils;
import com.ohos.trebleshot.utils.NetworkDeviceLoader;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.elements.TextElement;
import me.panavtec.title.adapterBase.EditableListProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;
import ohos.wifi.WifiLinkedInfo;
import ohos.wifi.WifiScanInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkDeviceListProvider extends EditableListProvider<NetworkDeviceListProvider.EditableNetworkDevice, EditableListProvider.EditableViewHolder> {
    private ConnectionUtils mConnectionUtils;
    private TextElement.IShapeBuilder mIconBuilder;

    public NetworkDeviceListProvider(Context context, ConnectionUtils connectionUtils) {
        super(context);
        mConnectionUtils = connectionUtils;
        mIconBuilder = AppUtils.getDefaultIconBuilder(context);
    }

    @Override
    public List<EditableNetworkDevice> onLoad() {
        List<EditableNetworkDevice> list = new ArrayList<>();

        if (mConnectionUtils.canReadScanResults()) {
            for (WifiScanInfo resultIndex : mConnectionUtils.getWifiManager().getScanInfoList()) {
                if (!resultIndex.getSsid().startsWith(AppConfig.PREFIX_ACCESS_POINT))
                    continue;

                HotspotNetwork hotspotNetwork = new HotspotNetwork();

                hotspotNetwork.lastUsageTime = System.currentTimeMillis();
                hotspotNetwork.SSID = resultIndex.getSsid();
                hotspotNetwork.BSSID = resultIndex.getBssid();
                hotspotNetwork.nickname = AppUtils.getFriendlySSID(resultIndex.getSsid());

                list.add(hotspotNetwork);
            }
        }

        if (list.size() == 0 && mConnectionUtils.isConnectionSelfNetwork()) {
            WifiLinkedInfo wifiInfo = mConnectionUtils.getWifiManager().getLinkedInfo().get();

            HotspotNetwork hotspotNetwork = new HotspotNetwork();

            hotspotNetwork.lastUsageTime = System.currentTimeMillis();
            hotspotNetwork.SSID = wifiInfo.getSsid();
            hotspotNetwork.BSSID = wifiInfo.getBssid();
            hotspotNetwork.nickname = AppUtils.getFriendlySSID(wifiInfo.getSsid());

            list.add(hotspotNetwork);
        }

        for (EditableNetworkDevice device : AppUtils.getDatabase(mContext).castQuery(new SQLQuery.Select(AccessDatabase.TABLE_DEVICES)
                .setOrderBy(AccessDatabase.FIELD_DEVICES_LASTUSAGETIME + " DESC"), EditableNetworkDevice.class))
            if (filterItem(device) && (!device.isLocalAddress) || AppUtils.getPreferences(mContext).getBoolean("developer_mode", false))
                list.add(device);

        return list;
    }


    //@Override
//    public EditableListProvider.EditableViewHolder onCreateViewHolder(Component parent, int viewType) {
//        return new EditableListProvider.EditableViewHolder(getScatter().parse(
//                isHorizontalOrientation() || isGridLayoutRequested()
//                        ? ResourceTable.Layout_list_network_device_grid
//                        : 0, parent, false));
//    }

    //@Override
    public void onBindViewHolder(EditableListProvider.EditableViewHolder holder, int position) {
        try {
            NetworkDevice device = getItem(position);
            Component parentView = holder.getComponent();
            boolean hotspotNetwork = device instanceof HotspotNetwork;

            Text deviceText = (Text) parentView.findComponentById(ResourceTable.Id_text2);
            Text userText = (Text) parentView.findComponentById(ResourceTable.Id_text1);
            Image userImage = (Image) parentView.findComponentById(ResourceTable.Id_image);
            Image statusImage = null;//(Image) parentView.findComponentById(ResourceTable.Id_imageStatus);

            userText.setText(device.nickname);
            deviceText.setText(hotspotNetwork ? mContext.getString(ResourceTable.String_text_trebleshotHotspot) : device.model);
            NetworkDeviceLoader.showPictureIntoView(device, userImage, mIconBuilder);

            if (device.isRestricted) {
                statusImage.setVisibility(Component.VISIBLE);
                statusImage.setPixelMap(ResourceTable.Graphic_ic_block_white_24dp);
            } else if (device.isTrusted) {
                statusImage.setVisibility(Component.VISIBLE);
                statusImage.setPixelMap(ResourceTable.Graphic_ic_vpn_key_white_24dp);
            } else {
                statusImage.setVisibility(Component.INVISIBLE);
            }
        } catch (NotReadyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyItemChanged(int position) {

    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {

    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        return null;
    }

    public static class EditableNetworkDevice extends NetworkDevice implements Editable {
        private boolean mIsSelected = false;

        public EditableNetworkDevice() {
            super();
        }

        @Override
        public boolean applyFilter(String[] filteringKeywords) {
            for (String keyword : filteringKeywords)
                if (nickname.toLowerCase().contains(keyword.toLowerCase()))
                    return true;

            return false;
        }

        @Override
        public boolean comparisonSupported() {
            return true;
        }

        @Override
        public long getId() {
            return deviceId.hashCode();
        }

        @Override
        public void setId(long id) {

        }

        @Override
        public String getComparableName() {
            return nickname;
        }

        @Override
        public long getComparableDate() {
            return lastUsageTime;
        }

        @Override
        public long getComparableSize() {
            return 0;
        }

        @Override
        public String getSelectableTitle() {
            return nickname;
        }

        @Override
        public boolean isSelectableSelected() {
            return mIsSelected;
        }

        @Override
        public boolean setSelectableSelected(boolean selected) {
            mIsSelected = selected;
            return true;
        }
    }

    public static class HotspotNetwork extends EditableNetworkDevice {
        public String SSID;
        public String BSSID;
        public String password;
        public int keyManagement;
        public boolean qrConnection;

        public HotspotNetwork() {
            super();

            this.versionName = "stamp";
            this.versionNumber = -1;
        }

        @Override
        public long getId() {
            return SSID.hashCode();
        }
    }
}
