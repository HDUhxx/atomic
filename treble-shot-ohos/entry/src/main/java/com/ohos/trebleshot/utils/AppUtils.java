package com.ohos.trebleshot.utils;

import com.ohos.trebleshot.config.AppConfig;
import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.log.Log;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.service.preference.SuperPreferences;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.elements.TextElement;
import ohos.account.AccountAbility;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.system.DeviceInfo;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ohos.trebleshot.config.AppConfig.PREFERENCES_FILE_NAME;

public class AppUtils {
    private static AccessDatabase mDatabase;
    private static int mUniqueNumber = 0;

    private static SuperPreferences mDefaultPreferences;
    private static SuperPreferences mDefaultLocalPreferences;
    private static SuperPreferences mViewingPreferences;

    public static void applyDeviceToJSON(Context context, JSONObject object) {
        NetworkDevice device = getLocalDevice(context);
        JSONObject deviceInformation = new JSONObject();
        JSONObject appInfo = new JSONObject();

        deviceInformation.put(Keyword.DEVICE_INFO_SERIAL, device.deviceId);
        deviceInformation.put(Keyword.DEVICE_INFO_BRAND, device.brand);
        deviceInformation.put(Keyword.DEVICE_INFO_MODEL, device.model);
        deviceInformation.put(Keyword.DEVICE_INFO_USER, device.nickname);


        appInfo.put(Keyword.APP_INFO_VERSION_CODE, device.versionNumber);
        appInfo.put(Keyword.APP_INFO_VERSION_NAME, device.versionName);

        object.put(Keyword.APP_INFO, appInfo);
        object.put(Keyword.DEVICE_INFO, deviceInformation);
    }

    public static void applyAdapterName(NetworkDevice.Connection connection) {
        if (connection.ipAddress == null) {
            Log.e(AppUtils.class.getSimpleName(), "Connection should be provided with IP address");
            return;
        }

        List<AddressedInterface> interfaceList = NetworkUtils.getInterfaces(true, AppConfig.DEFAULT_DISABLED_INTERFACES);

        for (AddressedInterface addressedInterface : interfaceList) {
            if (NetworkUtils.getAddressPrefix(addressedInterface.getAssociatedAddress())
                    .equals(NetworkUtils.getAddressPrefix(connection.ipAddress))) {
                connection.adapterName = addressedInterface.getNetworkInterface().getDisplayName();
                return;
            }
        }

        connection.adapterName = Keyword.Local.NETWORK_INTERFACE_UNKNOWN;
    }

    public static AccessDatabase getDatabase(Context context) {
        if (mDatabase == null)
            mDatabase = new AccessDatabase(context);

        return mDatabase;
    }

    public static Preferences getPreferences(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getPreferences(PREFERENCES_FILE_NAME);
    }

    public static String getDeviceSerial(Context context) {
        return AccountAbility.getAccountAbility().getDistributedVirtualDeviceId();
    }

    public static NetworkDevice getLocalDevice(Context context) {
        NetworkDevice device = new NetworkDevice(getDeviceSerial(context));
        device.brand = DeviceInfo.getName();
        device.model = DeviceInfo.getModel();
        device.nickname = "test";
        device.isTrusted = true;
        device.isRestricted = false;
        device.isLocalAddress = true;
        device.versionNumber = 98;
        device.versionName = "1.4.2";


        return device;
    }

    public static TextElement.IShapeBuilder getDefaultIconBuilder(Context context) {
        TextElement.IShapeBuilder builder = TextElement.builder();

        builder.beginConfig()
                .firstLettersOnly(true)
                .textMaxLength(1)
                .textColor(context.getColor(ResourceTable.Color_colorControlNormal))
                .shapeColor(context.getColor(ResourceTable.Color_colorPassive));

        return builder;
    }
    
    
    public static int getUniqueNumber() {
        return (int) (System.currentTimeMillis() / 1000) + (++mUniqueNumber);
    }

    public static String getLocalDeviceName(Context context) {
        return getPreferences(context)
                .getString("device_name", null);
    }

    public static ArrayList<File> getFiles(File file) {
        ArrayList<File> fileList = new ArrayList<>();
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory())    //若是目录，则递归打印该目录下的文件
                getFiles(f);
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        return fileList;
    }

    public static String getFriendlySSID(String ssid) {
        return ssid
                .replace("\"", "")
                .substring(AppConfig.PREFIX_ACCESS_POINT.length())
                .replace("_", " ");
    }


    public static SuperPreferences getViewingPreferences(Context context) {
        if (mViewingPreferences == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            Preferences preferences = databaseHelper.getPreferences(Keyword.Local.SETTINGS_VIEWING);
            //TODO Preferences to SharedPreferences
            mViewingPreferences = new SuperPreferences(null);
        }
        return mViewingPreferences;
    }


    public static String getHotspotName(Context context) {
        return AppConfig.PREFIX_ACCESS_POINT + AppUtils.getLocalDeviceName(context)
                .replaceAll(" ", "_");
    }

}
