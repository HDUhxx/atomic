package com.ohos.trebleshot.utils;

import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {


    public static File getUploadFile(Context context,String fileName){
        String str = (new SimpleDateFormat("yyyyMMddHHmmssSSS_")).format(new Date());
        String path = context.getFilesDir().getPath()+"/"+str+fileName;
        return new File(path);
    }

    public static boolean openUri(Context context, Uri uri) {
        return openUri(context, getOpenIntent(uri, ""));
    }

    public static Intent getOpenIntent(Uri url, String type)
    {
        return new Intent(new Intent().setUriAndType(url, type));
    }

    public static boolean openUri(Context context, Intent intent)
    {
        try {
            context.startAbility(intent, 0);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
//            HiLog.debug(TAG, String.format(Locale.US,
//                    "Open uri request failed with error message '%s'",
//                    e.getMessage()));
        }

        return false;
    }

    public static String sizeExpression(long bytes, boolean notUseByte)
    {
        int unit = notUseByte ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int expression = (int) (Math.log(bytes) / Math.log(unit));
        String prefix = (notUseByte ? "kMGTPE" : "KMGTPE").charAt(expression - 1) + (notUseByte ? "i" : "");

        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, expression), prefix);
    }


}
