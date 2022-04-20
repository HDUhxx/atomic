package com.ohos.trebleshot.service;

import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.CommonEventHelper;
import com.ohos.trebleshot.utils.NetworkUtils;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.wifi.WifiDevice;
import ohos.wifi.WifiLinkedInfo;

import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.HOURS;

public class DeviceDiscoveryTask {
    public static final String EVENT_DISCOVERY_END = "event_discovery_end";
    public static final String EVENT_DISCOVERY_STATUS = "event_discovery_status";
    private final Context context;
    private final ThreadPoolExecutor mExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private String wifiIpAddress;
    private boolean isRunning = false;
    public DeviceDiscoveryTask(Context context){
        this.context = context;
        WifiDevice device =  WifiDevice.getInstance(context);
        Optional<WifiLinkedInfo> optionalIpInfo =  device.getLinkedInfo();
        if(optionalIpInfo.isPresent()){
            int ipAddress = optionalIpInfo.get().getIpAddress();
            wifiIpAddress = NetworkUtils.intToIp(ipAddress);
        }

    }

    public void discovery(){
        if(isRunning()||wifiIpAddress==null)
            return;
        isRunning = true;
        String prefix = wifiIpAddress.substring(0, wifiIpAddress.lastIndexOf(".") + 1);
        for (int i = 0; i < 255; i++) {
            String testIp = prefix + i;
            mExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(300);
                        if(reachable)
                            UIConnectionUtils.setupConnectionSync(context,testIp,1,null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        new Thread(){
            @Override
            public void run(){
                try {
                    while (mExecutor.getActiveCount() != 0) { Thread.sleep(1000);}
                    CommonEventHelper.publish(EVENT_DISCOVERY_END,new Intent());
                    isRunning = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean isRunning(){
        if(wifiIpAddress==null)
            return false;
        return isRunning;
    }

    public void onStop(){
        mExecutor.shutdown();
    }
}
