package me.panavtec.title.hmutils;


import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.config.AppConfig;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.data.preferences.Preferences;
import ohos.utils.net.Uri;

import java.io.*;
import java.util.Locale;

public class FileUtils {
    public static String sizeExpression(long bytes, boolean notUseByte) {
        int unit = notUseByte ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int expression = (int) (Math.log(bytes) / Math.log(unit));
        String prefix = (notUseByte ? "kMGTPE" : "KMGTPE").charAt(expression - 1) + (notUseByte ? "i" : "");

        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, expression), prefix);
    }
//
//    public final String format(double number) {
//        // Android-removed: fast-path code.
//        return format(number, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
//    }

    public static void copy(Context context, File source, File destination,
                            int bufferLength, int socketTimeout) throws Exception {
        InputStream inputStream = new FileInputStream(source);
        OutputStream outputStream = new FileOutputStream(destination);

        if (inputStream == null || outputStream == null)
            throw new IOException("Failed to open streams to start copying");

        byte[] buffer = new byte[bufferLength];
        int len = 0;
        long lastRead = System.currentTimeMillis();

        while (len != -1) {
            if ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();

                lastRead = System.currentTimeMillis();
            }
        }

        outputStream.close();
        inputStream.close();
    }

    public static void copy(Context context, File source, File destination) throws Exception {
        copy(context, source, destination, AppConfig.BUFFER_LENGTH_DEFAULT, AppConfig.DEFAULT_SOCKET_TIMEOUT);
    }

    public static File getApplicationDirectory(Context context) {
        String defaultPath = getDefaultApplicationDirectoryPath(context);
        Preferences defaultPreferences = AppUtils.getPreferences(context);

        if (defaultPreferences.hasKey("text_storagePath")) {
            try {
                File savePath = new File(defaultPreferences.getString("text_storagePath", getDefaultApplicationDirectoryPath(context)));

                if (savePath.isDirectory() && savePath.canWrite())
                    return savePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File defaultFolder = new File(defaultPath);

        if (defaultFolder.isFile())
            defaultFolder.delete();

        if (!defaultFolder.isDirectory())
            defaultFolder.mkdirs();

        return defaultFolder;
    }

    public static String getDefaultApplicationDirectoryPath(Context context) {
        return "/data/data/com.loser007";
    }

    public static String getUniqueFileName(File storageFolder, String fileName, boolean tryActualFile) {
        if (tryActualFile && hasThisFileInFolder(storageFolder, fileName))
            return fileName;

        int pathStartPosition = fileName.lastIndexOf(".");

        String mergedName = pathStartPosition != -1 ? fileName.substring(0, pathStartPosition) : fileName;
        String fileExtension = pathStartPosition != -1 ? fileName.substring(pathStartPosition) : "";

        if (mergedName.length() == 0
                && fileExtension.length() > 0) {
            mergedName = fileExtension;
            fileExtension = "";
        }

        for (int exceed = 1; exceed < 999; exceed++) {
            String newName = mergedName + " (" + exceed + ")" + fileExtension;

            if (hasThisFileInFolder(storageFolder, newName))
                return newName;
        }

        return fileName;
    }

    public static boolean hasThisFileInFolder(File folder, String fileName){
        if(folder.isDirectory()){
            String[] files =  folder.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.equals(fileName);
                }
            });
            if(files != null) return files.length > 0;
        }
        return false;
    }

    public static Uri getUri(Uri orgUri, String path) {
        return Uri.appendEncodedPathToUri(orgUri, path);
    }

    /**
     * vp转像素
     *
     * @param context 。
     * @param vp
     * @return int
     */
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }

    public static void openFile(Context cx,File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
//        intent.setAction(Intent.ACTION_BOOK_TRAIN_TICKET);  //设置intent的Action属性
        String type = getMIMEType(file);  //获取文件file的MIME类型
        intent.setUriAndType(/*uri*/Uri.getUriFromFile(file), type);   //设置intent的data和Type属性。
        System.out.println("type>>>>>>>>>>>>>"+type);
        System.out.println("Uri>>>>>>>>>>>>>"+Uri.getUriFromFile(file).toString());
        cx.startAbility(intent,0);    //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
    }
    private static String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");  //获取后缀名前的分隔符"."在fName中的位置。
        if(dotIndex < 0){
            return type;
        }

        String end=fName.substring(dotIndex).toLowerCase();  /* 获取文件的后缀名*/
        if(end == null ||"".equals(end))return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i = 0; i< MIME_MAP_TABLE.length; i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MAP_TABLE[i][0]))
                type = MIME_MAP_TABLE[i][1];
        }
        return type;
    }
    public static final String[][] MIME_MAP_TABLE ={
            //{后缀名，MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",      "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz",     "application/x-gzip"},
            {".h",      "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js",     "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc",     "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh",     "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",      "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
}
