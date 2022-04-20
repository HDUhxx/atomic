package com.ohos.trebleshot.utils;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.database.SQLiteDatabase;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.ShowingAssignee;
import com.ohos.trebleshot.object.TransferObject;
import ohos.app.Context;
import ohos.data.rdb.ValuesBucket;

import java.util.List;

public class TransferUtils {

    public static long createUniqueTransferId(long groupId, String deviceId, TransferObject.Type type)
    {
        return String.format("%d_%s_%s", groupId, deviceId, type).hashCode();
    }


    public static SQLQuery.Select createTransferSelection(long groupId, String deviceId)
    {
        return new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(String.format("%s = ? AND %s = ?",
                        AccessDatabase.FIELD_TRANSFER_GROUPID,
                        AccessDatabase.FIELD_TRANSFER_DEVICEID),
                        String.valueOf(groupId), deviceId);
    }

    public static SQLQuery.Select createTransferSelection(long groupId, String deviceId, TransferObject.Flag flag, boolean equals)
    {
        return new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(String.format("%s = ? AND %s = ? AND %s " + (equals ? "=" : "!=") + " ?",
                        AccessDatabase.FIELD_TRANSFER_GROUPID,
                        AccessDatabase.FIELD_TRANSFER_DEVICEID,
                        AccessDatabase.FIELD_TRANSFER_FLAG),
                        String.valueOf(groupId), deviceId, flag.toString());
    }

    public static List<ShowingAssignee> loadAssigneeList(SQLiteDatabase database, long groupId)
    {
        SQLQuery.Select select = new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERASSIGNEE)
                .setWhere(AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID + "=?", String.valueOf(groupId));

        return database.castQuery(select, ShowingAssignee.class, new SQLiteDatabase.CastQueryListener<ShowingAssignee>()
        {
            @Override
            public void onObjectReconstructed(SQLiteDatabase db, CursorItem item, ShowingAssignee object)
            {
                object.device = new NetworkDevice(object.deviceId);
                object.connection = new NetworkDevice.Connection(object);

                try {
                    db.reconstruct(object.device);
                } catch (Exception e) {
                    // Nope
                }

                try {
                    db.reconstruct(object.connection);
                } catch (Exception e) {
                    // Nope
                }
            }
        });
    }

    public static TransferObject fetchValidTransfer(Context context, long groupId,
                                                    TransferObject.Type type)
    {
        CursorItem receiverInstance = AppUtils.getDatabase(context).getFirstFromTable(new SQLQuery
                .Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(AccessDatabase.FIELD_TRANSFER_TYPE + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_FLAG + "=?",
                        type.toString(),
                        String.valueOf(groupId),
                        TransferObject.Flag.PENDING.toString())
                .setOrderBy(String.format("`%s` ASC, `%s` ASC",
                        AccessDatabase.FIELD_TRANSFER_DIRECTORY,
                        AccessDatabase.FIELD_TRANSFER_NAME)));

        return receiverInstance == null
                ? null
                : new TransferObject(receiverInstance);
    }

    public static TransferObject fetchValidTransfer(Context context, long groupId,
                                                    String deviceId,
                                                    TransferObject.Type type)
    {
        CursorItem receiverInstance = AppUtils.getDatabase(context).getFirstFromTable(new SQLQuery
                .Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(AccessDatabase.FIELD_TRANSFER_TYPE + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_DEVICEID + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_FLAG + "=?",
                        type.toString(),
                        String.valueOf(groupId),
                        deviceId,
                        TransferObject.Flag.PENDING.toString())
                .setOrderBy(String.format("`%s` ASC, `%s` ASC",
                        AccessDatabase.FIELD_TRANSFER_DIRECTORY,
                        AccessDatabase.FIELD_TRANSFER_NAME)));

        return receiverInstance == null
                ? null
                : new TransferObject(receiverInstance);
    }

    public static void recoverIncomingInterruptions(Context context, long groupId)
    {
        ValuesBucket valuesBucket = new ValuesBucket();

        valuesBucket.putString(AccessDatabase.FIELD_TRANSFER_FLAG, TransferObject.Flag.PENDING.toString());

        AppUtils.getDatabase(context).update(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(AccessDatabase.FIELD_TRANSFER_GROUPID + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_FLAG + "=? AND "
                                + AccessDatabase.FIELD_TRANSFER_TYPE + "=?",
                        String.valueOf(groupId),
                        TransferObject.Flag.INTERRUPTED.toString(),
                        TransferObject.Type.INCOMING.toString()), valuesBucket);
    }
}
