package com.ohos.trebleshot.utils;

import ohos.app.Context;
import ohos.wifi.WifiDevice;
import ohos.wifi.WifiLinkedInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class HotspotUtils
{
    private static final String TAG = "HotspotUtils";

    private static HotspotUtils mInstance = null;

    private WifiDevice mWifiManager;

    private HotspotUtils(Context context)
    {
        mWifiManager = WifiDevice.getInstance(context);
    }

    public static HotspotUtils getInstance(Context context)
    {
        if (mInstance == null)
            mInstance = new OreoAPI(context);

        return mInstance;
    }

    private static Object invokeSilently(Method method, Object receiver, Object... args)
    {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            HiLog.error(TAG, "exception in invoking methods: " + method.getName() + "(): " + e.getMessage());
        }

        return null;
    }

    public static boolean isSupported()
    {
        return true;
    }

    public WifiDevice getWifiManager()
    {
        return mWifiManager;
    }

    abstract public boolean disable();

    abstract public boolean enable();

    abstract public boolean enableConfigured(String apName, String passKeyWPA2);

    abstract public WifiLinkedInfo getConfiguration();

    abstract public WifiLinkedInfo getPreviousConfig();

    abstract public boolean isEnabled();

    abstract public boolean isStarted();

    abstract public boolean unloadPreviousConfig();

    //@RequiresApi(26)
    public static class OreoAPI extends HotspotUtils
    {
//        private WifiDevice.LocalOnlyHotspotReservation mHotspotReservation;
//        private WifiDevice.LocalOnlyHotspotCallback mCallback;

        private OreoAPI(Context context)
        {
            super(context);
        }

        @Override
        public boolean disable()
        {
//            if (mHotspotReservation == null)
//                return false;
//
//            mHotspotReservation.close();
//            mHotspotReservation = null;

            return true;
        }

        @Override
        public boolean enable()
        {
            try {
//                getWifiManager().startLocalOnlyHotspot(new WifiDevice.LocalOnlyHotspotCallback()
//                {
//                    @Override
//                    public void onStarted(WifiDevice.LocalOnlyHotspotReservation reservation)
//                    {
//                        super.onStarted(reservation);
//                        mHotspotReservation = reservation;
//
//                        if (mCallback != null)
//                            mCallback.onStarted(reservation);
//                    }
//
//                    @Override
//                    public void onStopped()
//                    {
//                        super.onStopped();
//                        mHotspotReservation = null;
//
//                        if (mCallback != null)
//                            mCallback.onStopped();
//                    }
//
//                    @Override
//                    public void onFailed(int reason)
//                    {
//                        super.onFailed(reason);
//                        mHotspotReservation = null;
//
//                        if (mCallback != null)
//                            mCallback.onFailed(reason);
//                    }
//                }, new EventHandler(Looper.myLooper()));

                return true;
            } catch (Throwable e) {
            }

            return false;
        }

        @Override
        public WifiLinkedInfo getConfiguration()
        {
//            if (mHotspotReservation == null)
                return null;

//            return mHotspotReservation.getWifiConfiguration();
        }

        @Override
        public WifiLinkedInfo getPreviousConfig()
        {
            return getConfiguration();
        }

        @Override
        public boolean enableConfigured(String apName, String passKeyWPA2)
        {
            return enable();
        }

        @Override
        public boolean isEnabled()
        {
            return HackAPI.enabled(getWifiManager());
        }

        @Override
        public boolean isStarted()
        {
            return true;
//                    mHotspotReservation != null;
        }

//        public void setSecondaryCallback(WifiDevice.LocalOnlyHotspotCallback callback)
//        {
//            mCallback = callback;
//        }

        @Override
        public boolean unloadPreviousConfig()
        {

            return true;//mHotspotReservation != null;
        }
    }

    public static class HackAPI extends HotspotUtils
    {
        private static Method getWifiApConfiguration;
        private static Method getWifiApState;
        private static Method isWifiApEnabled;
        private static Method setWifiApEnabled;
        private static Method setWifiApConfiguration;

        static {
            for (Method method : WifiDevice.class.getDeclaredMethods()) {
                switch (method.getName()) {
                    case "getWifiApConfiguration":
                        getWifiApConfiguration = method;
                        break;
                    case "getWifiApState":
                        getWifiApState = method;
                        break;
                    case "isWifiApEnabled":
                        isWifiApEnabled = method;
                        break;
                    case "setWifiApEnabled":
                        setWifiApEnabled = method;
                        break;
                    case "setWifiApConfiguration":
                        setWifiApConfiguration = method;
                        break;
                }
            }
        }

        private WifiLinkedInfo mPreviousConfig;

        private HackAPI(Context context)
        {
            super(context);
        }

        public static boolean enabled(WifiDevice wifiManager)
        {
            Object result = invokeSilently(isWifiApEnabled, wifiManager);

            if (result == null)
                return false;

            return (Boolean) result;
        }

        public static boolean supported()
        {
            return getWifiApState != null
                    && isWifiApEnabled != null
                    && setWifiApEnabled != null
                    && getWifiApConfiguration != null;
        }

        public boolean disable()
        {
            unloadPreviousConfig();
            return setHotspotEnabled(mPreviousConfig, false);
        }

        public boolean enable()
        {
//            getWifiManager().setWifiEnabled(false);
            return setHotspotEnabled(getConfiguration(), true);
        }

        public boolean enableConfigured(String apName, String passKeyWPA2)
        {
//            getWifiManager().setWifiEnabled(false);
//
//            if (mPreviousConfig == null)
//                mPreviousConfig = getConfiguration();
//
//            WifiLinkedInfo wifiConfiguration = new WifiLinkedInfo();
//
//            wifiConfiguration.SSID = apName;
//
//            if (passKeyWPA2 != null && passKeyWPA2.length() >= 8) {
//                wifiConfiguration.allowedKeyManagement.set(WifiLinkedInfo.KeyMgmt.WPA_PSK);
//                wifiConfiguration.allowedAuthAlgorithms.set(WifiLinkedInfo.AuthAlgorithm.OPEN);
//                wifiConfiguration.preSharedKey = passKeyWPA2;
//            } else
//                wifiConfiguration.allowedKeyManagement.set(WifiLinkedInfo.KeyMgmt.NONE);

            return setHotspotEnabled(null, true);
        }

        @Override
        public boolean isEnabled()
        {
            return enabled(getWifiManager());
        }

        @Override
        public boolean isStarted()
        {
            return getPreviousConfig() != null;
        }

        public WifiLinkedInfo getConfiguration()
        {
            return (WifiLinkedInfo) invokeSilently(getWifiApConfiguration, getWifiManager());
        }

        public WifiLinkedInfo getPreviousConfig()
        {
            return mPreviousConfig;
        }

        private boolean setHotspotConfig(WifiLinkedInfo config)
        {
            Object result = invokeSilently(setWifiApConfiguration, getWifiManager(), config);

            if (result == null)
                return false;

            return (Boolean) result;
        }

        private boolean setHotspotEnabled(WifiLinkedInfo config, boolean enabled)
        {
            Object result = invokeSilently(setWifiApEnabled, getWifiManager(), config, enabled);

            if (result == null)
                return false;

            return (Boolean) result;
        }

        public boolean unloadPreviousConfig()
        {
            if (mPreviousConfig == null)
                return false;

            setHotspotConfig(mPreviousConfig);

            mPreviousConfig = null;

            return true;
        }
    }
}
