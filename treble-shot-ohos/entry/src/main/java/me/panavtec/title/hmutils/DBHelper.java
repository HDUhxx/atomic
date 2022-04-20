package me.panavtec.title.hmutils;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.TransferObject;
import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.Entity.PreloadedGroup;
import ohos.app.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper {

    /**
     * 根据groundId查询数据库
     *
     * @param context  上下文
     * @param groundId id
     * @return 传输对象信息
     */
    public static List<TransferObject> queryDataByGroundId(Context context, String groundId) {
        if (TextUtils.isEmpty(groundId)) return new ArrayList<>();
        String exr = AccessDatabase.FIELD_TRANSFER_GROUPID + "=?";
        return AppUtils.getDatabase(context).castQuery(
                new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER).setWhere(exr,
                        groundId), TransferObject.class);
    }

    public static List<TransferObject> queryDataByRequestId(Context context, String requestId) {
        if (TextUtils.isEmpty(requestId)) return new ArrayList<>();
        String exr = AccessDatabase.FIELD_TRANSFER_ID + "=?";
        return AppUtils.getDatabase(context).castQuery(
                new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER).setWhere(exr,
                        requestId), TransferObject.class);
    }


    public static List<TransferObject> queryDataByGRId(Context context, String requestId, String groundId) {
        if (TextUtils.isEmpty(requestId)) return new ArrayList<>();
        String exr = AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND " + AccessDatabase.FIELD_TRANSFER_ID + "=?";
        return AppUtils.getDatabase(context).castQuery(new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER).setWhere(exr, groundId, requestId), TransferObject.class);
    }

    /**
     * @param baseSelectEntity 数据保存实体
     * @return 传输存入数据库的实体
     */
    public static TransferObject getTransferObject(BaseSelectEntity baseSelectEntity, long groundId) {
        TransferObject object = new TransferObject();
        object.groupId = groundId;
        object.accessPort = 58732;
        object.file = baseSelectEntity.getData();
        object.fileMimeType = baseSelectEntity.getMineType();
        object.fileSize = Long.parseLong(baseSelectEntity.getSize());
        object.friendlyName = baseSelectEntity.getDisplayName();
        object.requestId = baseSelectEntity.getMediaId() + AppUtils.getUniqueNumber();
        object.type = TransferObject.Type.OUTGOING;
        object.flag = TransferObject.Flag.PENDING;
        object.directory = baseSelectEntity.getUri().toString();
        return object;
    }


    /**
     * @param pathList 文件夹路径
     * @return 传输存入数据库的实体
     */
    public static List<TransferObject> getTransferObjectForFolder(List<String> pathList, long groundId) {
        if (pathList == null || pathList.size() == 0) return new ArrayList<>();
        List<TransferObject> needList = new ArrayList<>();
        TransferObject object;
        for (String s : pathList) {
            if (TextUtils.isEmpty(s)) continue;
            File file1 = new File(s);
            if (!file1.isDirectory()) {
                object = new TransferObject();
                object.groupId = groundId;
                object.accessPort = 58732;
                object.file = s;
                System.out.println("===================" + file1.getName() + "xxxxxxxxx");
//                String[] files = new String[0];


                if (file1.getName().contains(".")) {
                    String[] files = file1.getName().split("\\.");
                    System.out.println(files.length + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    for (int i = 0; i < files.length; i++) {
                        System.out.println(files[i]);
                    }
                    object.fileMimeType = file1.getName().split("\\.")[files.length - 1];
                } else {
                    object.fileMimeType = ".txt";
                }
                object.fileSize = file1.length();
                object.friendlyName = file1.getName();
                object.requestId = AppUtils.getUniqueNumber();
                object.type = TransferObject.Type.OUTGOING;
                object.flag = TransferObject.Flag.PENDING;
//            object.directory = s;
//                object.requestId = groundId;
                needList.add(object);
            } else {
                ArrayList<File> files = AppUtils.getFiles(new File(s));
                for (File file : files) {
                    object = new TransferObject();
                    object.groupId = groundId;
                    object.accessPort = 58732;
                    object.file = file.getAbsolutePath();
                    if (file.getName().contains("."))
                        object.fileMimeType = file.getName().split(".")[1];
                    object.fileSize = file.length();
                    object.friendlyName = file.getName();
                    object.requestId = AppUtils.getUniqueNumber();
                    object.type = TransferObject.Type.OUTGOING;
                    object.flag = TransferObject.Flag.PENDING;
//            object.directory = s;
//                    object.requestId = groundId;
                    needList.add(object);
                }
            }
        }
        return needList;
    }


    /**
     * 转换为界面面数据
     *
     * @param transferObjects 传输对象信息
     * @return 界面数据
     */
    public static List<PreloadedGroup> convertViewData(List<TransferObject> transferObjects) {
        ArrayList<PreloadedGroup> list = new ArrayList<>();
        PreloadedGroup transferEntity;
        if (transferObjects.size() == 0) return list;
        for (TransferObject transferObject : transferObjects) {
            transferEntity = new PreloadedGroup();
            transferEntity.setGroupId(transferObject.groupId);
            transferEntity.setAssignees(transferObject.friendlyName);
            transferEntity.setTitle(transferObject.friendlyName);
            transferEntity.setSize(transferObject.fileSize + "");
            transferEntity.setSavePath(transferObject.directory);
            transferEntity.setMineType(transferObject.fileMimeType);
            transferEntity.setGroupId(transferObject.groupId);
            transferEntity.setRequestId(transferObject.requestId);
            transferEntity.type = transferObject.type;
            transferEntity.flag = transferObject.flag;
            list.add((transferEntity));
        }
        return list;
    }


    /**
     * 删除与界面对应的数据库中的数据
     *
     * @param selectedList ，
     * @param context      。
     */
    public static void deleteTransferInDb(List<PreloadedGroup> selectedList, Context context) {
        if (selectedList == null || selectedList.size() == 0) return;
        List<TransferObject> deleteList = new ArrayList<>();
        for (PreloadedGroup preloadedGroup : selectedList) {
            List<TransferObject> transferObjects = DBHelper.queryDataByRequestId(context, String.valueOf(preloadedGroup.getRequestId()));
            if (transferObjects != null && transferObjects.size() > 0) deleteList.addAll(transferObjects);
        }
        Toast.show(context, "删除" + deleteList.size());
        AppUtils.getDatabase(context).remove(deleteList);
    }

    public static long getGroupId() {
//        return AppUtils.getUniqueNumber();
        return AppUtils.getUniqueNumber();
    }


    public static List<TransferObject> queryDataAll(Context context, String groundId) {
        if (TextUtils.isEmpty(groundId)) return new ArrayList<>();
        String exr = AccessDatabase.FIELD_TRANSFER_GROUPID + "=?";
        HashMap<String, TransferObject> map = new HashMap<>();
        List<TransferObject> transferObjects = AppUtils.getDatabase(context).castQuery(
                new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER).setWhere(exr,
                        groundId), TransferObject.class);
        List<TransferObject> transferObjects1 = AppUtils.getDatabase(context).castQuery(
                new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER).setWhere(exr,
                        groundId), TransferObject.class);
        if (transferObjects != null && transferObjects.size() > 0) {
            for (TransferObject transferObject : transferObjects) {
                if (transferObject.requestId == 0L) continue;
                String s = String.valueOf(transferObject.requestId);
                map.put(s, transferObject);
            }
        }

        if (transferObjects1 != null && transferObjects1.size() > 0) {
            for (TransferObject transferObject : transferObjects1) {
                if (transferObject.requestId == 0L) continue;
                String s = String.valueOf(transferObject.requestId);
                map.put(s, transferObject);
            }
        }
        return new ArrayList<>(map.values());
    }

}
