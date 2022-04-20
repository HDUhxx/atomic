package com.developer.filepicker.utils;

import com.developer.filepicker.model.FileListItem;
import ohos.app.Context;
import ohos.bundle.IBundleManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * @author akshay sunil masram
 */
public class Utility {

    public static boolean checkStorageAccessPermissions(Context context) {
        if (context.verifySelfPermission("ohos.permission.WRITE_MEDIA") == IBundleManager.PERMISSION_GRANTED
                && context.verifySelfPermission("ohos.permission.READ_MEDIA") == IBundleManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // 权限已被授予
            return false;
        }
    }

    public static ArrayList<FileListItem>
    prepareFileListEntries(ArrayList<FileListItem> internalList, File inter,
                           ExtensionFilter filter, boolean show_hidden_files) {
        for (File name : Objects.requireNonNull(inter.listFiles(filter))) {
            if (name.canRead()) {
                if (name.getName().startsWith(".") && !show_hidden_files) {
                    continue;
                }
                FileListItem item = new FileListItem();
                item.setFilename(name.getName());
                item.setDirectory(name.isDirectory());
                try {
                    item.setLocation(name.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                item.setTime(name.lastModified());
                internalList.add(item);
            }
        }
        Collections.sort(internalList);
        return internalList;
    }
}
