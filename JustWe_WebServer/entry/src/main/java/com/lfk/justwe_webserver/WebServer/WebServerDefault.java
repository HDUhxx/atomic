package com.lfk.justwe_webserver.WebServer;

import ohos.app.Context;
import ohos.app.Environment;

import java.io.File;

/**
 * Created by liufengkai on 16/1/6.
 */
public class WebServerDefault {

    public static final String WebServerServiceConnected = "Service connected";

    public static final String WebServerServiecDisconnected = "Service disconnected";

    public static final int WebDefaultPort = 8080;

    public static String WebServerFiles;

    public static String getWebServerFiles() {
        return WebServerFiles;
    }

    public static String WebServerIp;

    public static Context context;

    public static void init(Context context) {
        WebServerDefault.context = context;
        WebServerFiles = context.getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS) + "/JustWeWebServer";
        File file = new File(WebServerFiles);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 转换IP
     *
     * @param i Ip字符串
     * @return 字符串
     */
    public static String intToIp(int i) {
        return ((i) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static void setWebServerIp(String webServerIp) {
        WebServerIp = webServerIp;
    }
}
