package com.ohos.trebleshot.utils;

import com.ohos.trebleshot.config.AppConfig;
import me.panavtec.title.hmadapter.NetworkDeviceListProvider;
import ohos.ai.cv.common.ConnectionCallback;
import ohos.app.Context;
import ohos.location.Locator;
import ohos.net.ConnectionProperties;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.wifi.*;

import java.util.Optional;

/**
 * created by: veli
 * date: 15/04/18 18:37
 */
public class ConnectionUtils {
    public static final String TAG = ConnectionUtils.class.getSimpleName();

    private Context mContext;
    private WifiDevice mWifiManager;
    private HotspotUtils mHotspotUtils;
    private Locator mLocationManager;
    private NetManager mConnectivityManager;

    ConnectionUtils(Context context) {
        mContext = context;

        mWifiManager = WifiDevice.getInstance(context);
        mLocationManager = new Locator(context);
        mHotspotUtils = HotspotUtils.getInstance(context);
        mConnectivityManager = NetManager.getInstance(context);
        
    }

    public static ConnectionUtils getInstance(Context context) {
        return new ConnectionUtils(context);
    }

    public static String getCleanNetworkName(String networkName) {
        if (networkName == null)
            return "";

        return networkName.replace("\"", "");
    }

    public boolean canAccessLocation() {
        return hasLocationPermission(getContext()) && isLocationServiceEnabled();
    }

    public boolean canReadScanResults() {
        return getWifiManager().isWifiActive();
    }

    public boolean disableCurrentNetwork() {
        // TODO: Networks added by other applications will possibly reconnect even if we disconnect them
        // This is because we are only allowed to manipulate the connections that we added.
        // And if it is the case, then the return value of disableNetwork will be false.
        return isConnectedToAnyNetwork();
//                && getWifiManager().disableNetwork()
//                && getWifiManager().disableNetwork(getWifiManager().getConnectionInfo().getNetworkId());
    }

    public String establishHotspotConnection(final Interrupter interrupter,
                                             final NetworkDeviceListProvider.HotspotNetwork hotspotNetwork,
                                             final ConnectionCallback connectionCallback) {
        final int pingTimeout = 1000; // ms
        final long startTime = System.currentTimeMillis();

        String remoteAddress = null;
        boolean connectionToggled = false;
        boolean secondAttempt = false;
        boolean thirdAttempt = false;

        while (true) {
            int passedTime = (int) (System.currentTimeMillis() - startTime);

            // retry code will be here.
            if (passedTime >= 10000 && !secondAttempt) {
                secondAttempt = true;
                disableCurrentNetwork();
                connectionToggled = false;
            }

            if (passedTime >= 20000 && !thirdAttempt) {
                thirdAttempt = true;
                disableCurrentNetwork();
                connectionToggled = false;
            }

            if (!getWifiManager().isWifiActive()) {
//                HiLog.debug(TAG, "establishHotspotConnection(): Wifi is off. Making a request to turn it on");
//
//                if (!getWifiManager().setWifiEnabled(true)) {
////                    HiLog.debug(TAG, "establishHotspotConnection(): Wifi was off. The request has failed. Exiting.");
//                    break;
//                }
            } else if (!isConnectedToNetwork(hotspotNetwork) && !connectionToggled) {
//                HiLog.debug(TAG, "establishHotspotConnection(): Requested network toggle");
                toggleConnection(hotspotNetwork);
                connectionToggled = true;
            } else {
//                HiLog.debug(TAG, "establishHotspotConnection(): Waiting to connect to the server");
                final Optional<IpInfo> routeInfo = getWifiManager().getIpInfo();
                //HiLog.w(TAG, String.format("establishHotspotConnection(): DHCP: %s", routeInfo));

                if (routeInfo != null && routeInfo.get().getGateway() != 0) {
                    final String testedRemoteAddress = NetworkUtils.convertInet4Address(routeInfo.get().getGateway());

//                    HiLog.debug(TAG, String.format("establishHotspotConnection(): DhcpInfo: gateway: %s dns1: %s dns2: %s ipAddr: %s serverAddr: %s netMask: %s",
//                            testedRemoteAddress,
//                            NetworkUtils.convertInet4Address(routeInfo.dns1),
//                            NetworkUtils.convertInet4Address(routeInfo.dns2),
//                            NetworkUtils.convertInet4Address(routeInfo.ipAddress),
//                            NetworkUtils.convertInet4Address(routeInfo.serverAddress),
//                            NetworkUtils.convertInet4Address(routeInfo.netmask)));

//                    HiLog.debug(TAG, "establishHotspotConnection(): There is DHCP info provided waiting to reach the address " + testedRemoteAddress);

                    if (NetworkUtils.ping(testedRemoteAddress, pingTimeout)) {
//                        HiLog.debug(TAG, "establishHotspotConnection(): AP has been reached. Returning OK state.");
                        remoteAddress = testedRemoteAddress;
                        break;
                    } else {
                        //HiLog.debug(TAG, "establishHotspotConnection(): Connection check ping failed");
                    }

                    if ( NetworkUtils.ping(testedRemoteAddress, pingTimeout)) {
//                            : NetworkUtils.ping(testedRemoteAddress)) {
//                        HiLog.debug(TAG, "establishHotspotConnection(): AP has been reached. Returning OK state.");
                        remoteAddress = testedRemoteAddress;
                        break;
                    } else {}
//                        HiLog.debug(TAG, "establishHotspotConnection(): Connection check ping failed");

                } else{}
//                    HiLog.debug(TAG, "establishHotspotConnection(): No DHCP provided. Looping...");
            }

            if (connectionCallback.onTimePassed(1000, passedTime) || interrupter.interrupted()) {
//                HiLog.debug(TAG, "establishHotspotConnection(): Timed out or onTimePassed returned true. Exiting...");
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        return remoteAddress;
    }

    public boolean hasLocationPermission(Context context) {
        return true;
        //return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public Context getContext() {
        return mContext;
    }

    public NetManager getConnectivityManager() {
        return mConnectivityManager;
    }

    public HotspotUtils getHotspotUtils() {
        return mHotspotUtils;
    }

    public Locator getLocationManager() {
        return mLocationManager;
    }

    public WifiDevice getWifiManager() {
        return mWifiManager;
    }

    public boolean isConnectionSelfNetwork() {
        Optional<WifiLinkedInfo> wifiInfo = getWifiManager().getLinkedInfo();

        return wifiInfo != null
                && getCleanNetworkName(wifiInfo.get().getSsid()).startsWith(AppConfig.PREFIX_ACCESS_POINT);
    }

    public boolean isConnectedToAnyNetwork() {
        NetHandle netHandle = getConnectivityManager().getDefaultNet();
        ConnectionProperties info = getConnectivityManager().getConnectionProperties(netHandle);
        return info != null;
//                && info.== ConnectivityManager.TYPE_WIFI
//                && info.getLinkAddresses().get(0).;
    }

    public boolean isConnectedToNetwork(NetworkDeviceListProvider.HotspotNetwork hotspotNetwork) {
        if (!isConnectedToAnyNetwork())
            return false;

        if (hotspotNetwork.BSSID != null)
            return hotspotNetwork.BSSID.equals(getWifiManager().getLinkedInfo().get().getBssid());

        return hotspotNetwork.SSID.equals(getCleanNetworkName(getWifiManager().getLinkedInfo().get().getSsid()));
    }

    public boolean isLocationServiceEnabled() {
        return true;// mLocationManager.isProviderEnabled(Locator.NETWORK_PROVIDER);
    }

    public boolean isMobileDataActive() {
        return mConnectivityManager.getDefaultNet() != null;
                //&& mConnectivityManager.getDefaultNet().getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean toggleConnection(NetworkDeviceListProvider.HotspotNetwork hotspotNetwork) {
        if (!isConnectedToNetwork(hotspotNetwork)) {
            if (isConnectedToAnyNetwork())
                disableCurrentNetwork();

            /*WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();*/

            WifiDevice wifiDevice = WifiDevice.getInstance(mContext);
            WifiDeviceConfig config = new WifiDeviceConfig();
            config.setSsid("untrusted-exist");
            config.setPreSharedKey("123456789");
            config.setHiddenSsid(false);
            config.setSecurityType(WifiSecurity.PSK);


//            WifiConfiguration config = new WifiConfiguration();
//
//            config.SSID = String.format("\"%s\"", hotspotNetwork.SSID);
//
//            switch (hotspotNetwork.keyManagement) {
//                case 0: // OPEN
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                    break;
//                case 1: // WEP64
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//
//                    if (hotspotNetwork.password != null
//                            && hotspotNetwork.password.matches("[0-9A-Fa-f]*")) {
//                        config.wepKeys[0] = hotspotNetwork.password;
//                    } else {
//                        //fail("Please type hex pair for the password");
//                    }
//                    break;
//                case 2: // WEP128
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//
//                    if (hotspotNetwork.password != null
//                            && hotspotNetwork.password.matches("[0-9A-Fa-f]*")) {
//                        config.wepKeys[0] = hotspotNetwork.password;
//                    } else {
//                        //fail("Please type hex pair for the password");
//                    }
//                    break;
//                case 3: // WPA_TKIP
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//
//                    if (hotspotNetwork.password != null
//                            && hotspotNetwork.password.matches("[0-9A-Fa-f]{64}")) {
//                        config.preSharedKey = hotspotNetwork.password;
//                    } else {
//                        config.preSharedKey = '"' + hotspotNetwork.password + '"';
//                    }
//                    break;
//                case 4: // WPA2_AES
//                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//
//                    if (hotspotNetwork.password != null
//                            && hotspotNetwork.password.matches("[0-9A-Fa-f]{64}")) {
//                        config.preSharedKey = hotspotNetwork.password;
//                    } else {
//                        config.preSharedKey = '"' + hotspotNetwork.password + '"';
//                    }
//                    break;
//            }

            /*
            old wifi connectivity code works for below M
            int netId = getWifiManager().addNetwork(config);

            getWifiManager().disconnect();
            getWifiManager().enableNetwork(netId, true);

            return getWifiManager().reconnect();*/

//            try {
//
//                boolean isSuccess = wifiDevice.addUntrustedConfig(config);
//                int netId = getWifiManager().addNetwork(config);
//
//                if (/*Build.VERSION.SDK_INT >= */UIConnectionUtils.isOSAbove(Build.VERSION_CODES.M)) {
//                    List<WifiConfiguration> list = getWifiManager().getConfiguredNetworks();
//                    for (WifiConfiguration hotspotWifi : list) {
//                        if (hotspotWifi.SSID != null && hotspotWifi.SSID.equalsIgnoreCase(config.SSID)) {
//                            getWifiManager().disconnect();
//                            getWifiManager().enableNetwork(hotspotWifi.networkId, true);
//                            return getWifiManager().reconnect();
//                        }
//                    }
//                } else {
//                    getWifiManager().disconnect();
//                    getWifiManager().enableNetwork(netId, true);
//                    return getWifiManager().reconnect();
//                }
//            } catch (Exception exp) {
//                disableCurrentNetwork();
//                return false;
//            }
//        }
//
//        disableCurrentNetwork();

            return false;
        }
        return false;
    }

    public  interface ConnectionCallback {
        boolean onTimePassed(int delimiter, long timePassed);
    }
}
