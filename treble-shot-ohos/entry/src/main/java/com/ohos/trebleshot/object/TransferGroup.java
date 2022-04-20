package com.ohos.trebleshot.object;

import com.ohos.trebleshot.database.*;
import com.ohos.trebleshot.utils.TransferUtils;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;

import java.util.ArrayList;
import java.util.List;

/**
 * created by: veli
 * date: 06.04.2018 09:37
 */
public class TransferGroup implements DatabaseObject<NetworkDevice>, Selectable {
    public long groupId;
    public long dateCreated;
    public String savePath;
    public boolean isServedOnWeb;

    private boolean mIsSelected = false;

    public TransferGroup() {
    }

    public TransferGroup(long groupId) {
        this.groupId = groupId;
    }

    public TransferGroup(CursorItem item) {
        reconstruct(item);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TransferGroup && ((TransferGroup) obj).groupId == groupId;
    }

    @Override
    public void reconstruct(CursorItem item) {
        this.groupId = item.getLong(AccessDatabase.FIELD_TRANSFERGROUP_ID);
        this.savePath = item.getString(AccessDatabase.FIELD_TRANSFERGROUP_SAVEPATH);
        this.dateCreated = item.getLong(AccessDatabase.FIELD_TRANSFERGROUP_DATECREATED);
        this.isServedOnWeb = item.getInt(AccessDatabase.FIELD_TRANSFERGROUP_ISSHAREDONWEB) == 1;
    }

    @Override
    public boolean isSelectableSelected() {
        return mIsSelected;
    }

    @Override
    public String getSelectableTitle() {
        return String.valueOf(groupId);
    }

    @Override
    public ValuesBucket getValues() {
        ValuesBucket values = new ValuesBucket();

        values.putLong(AccessDatabase.FIELD_TRANSFERGROUP_ID, groupId);
        values.putString(AccessDatabase.FIELD_TRANSFERGROUP_SAVEPATH, savePath);
        values.putLong(AccessDatabase.FIELD_TRANSFERGROUP_DATECREATED, dateCreated);
        values.putInteger(AccessDatabase.FIELD_TRANSFERGROUP_ISSHAREDONWEB, isServedOnWeb ? 1 : 0);

        return values;
    }

    @Override
    public SQLQuery.Select getWhere() {
        return new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERGROUP)
                .setWhere(AccessDatabase.FIELD_TRANSFERGROUP_ID + "=?", String.valueOf(groupId));
    }

    @Override
    public boolean setSelectableSelected(boolean selected) {
        mIsSelected = selected;
        return true;
    }

    @Override
    public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {
        this.dateCreated = System.currentTimeMillis();
    }

    @Override
    public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

    }

    @Override
    public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {
        database.remove(new SQLQuery.Select(AccessDatabase.DIVIS_TRANSFER)
                .setWhere(String.format("%s = ?", AccessDatabase.FIELD_TRANSFER_GROUPID), String.valueOf(groupId)));

        database.remove(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERASSIGNEE)
                .setWhere(AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID + "=?", String.valueOf(groupId)));

        database.removeAsObject(dbInstance, new SQLQuery.Select(AccessDatabase.TABLE_TRANSFER)
                .setWhere(AccessDatabase.FIELD_TRANSFER_GROUPID + "=?", String.valueOf(groupId)), TransferObject.class, null, this);
    }

    public static class Index {
        public boolean calculated = false;
        public boolean hasIssues = false;
        public long incoming = 0;
        public long incomingCompleted = 0;
        public long outgoing = 0;
        public long outgoingCompleted = 0;
        public int incomingCount = 0;
        public int outgoingCount = 0;
        public int incomingCountCompleted;
        public int outgoingCountCompleted;
        public final List<ShowingAssignee> assignees = new ArrayList<>();

        public void reset() {
            calculated = false;
            hasIssues = false;

            incoming = 0;
            outgoing = 0;
            incomingCount = 0;
            outgoingCount = 0;
            assignees.clear();
        }
    }

    public static class Assignee implements DatabaseObject<NetworkDevice> {
        public long groupId;
        public String deviceId;
        public String connectionAdapter;

        public Assignee() {

        }

        public Assignee(long groupId, String deviceId) {
            this.groupId = groupId;
            this.deviceId = deviceId;
        }

        public Assignee(TransferGroup group, NetworkDevice device) {
            this(group.groupId, device.deviceId);
        }

        public Assignee(long groupId, String deviceId, String connectionAdapter) {
            this(groupId, deviceId);
            this.connectionAdapter = connectionAdapter;
        }

        public Assignee(TransferGroup group, NetworkDevice device,
                        NetworkDevice.Connection connection) {
            this(group.groupId, device.deviceId, connection.adapterName);
        }

        @Override
        public SQLQuery.Select getWhere() {
            return new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERASSIGNEE)
                    .setWhere(AccessDatabase.FIELD_TRANSFERASSIGNEE_DEVICEID + "=? AND " + AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID + "=?", deviceId, String.valueOf(groupId));
        }

        @Override
        public ValuesBucket getValues() {
            ValuesBucket values = new ValuesBucket();

            values.putString(AccessDatabase.FIELD_TRANSFERASSIGNEE_DEVICEID, deviceId);
            values.putLong(AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID, groupId);
            values.putString(AccessDatabase.FIELD_TRANSFERASSIGNEE_CONNECTIONADAPTER, connectionAdapter);
            values.putInteger(AccessDatabase.FIELD_TRANSFERASSIGNEE_ISCLONE, 1);

            return values;
        }

        @Override
        public void reconstruct(CursorItem item) {
            this.deviceId = item.getString(AccessDatabase.FIELD_TRANSFERASSIGNEE_DEVICEID);
            this.groupId = item.getLong(AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID);
            this.connectionAdapter = item.getString(AccessDatabase.FIELD_TRANSFERASSIGNEE_CONNECTIONADAPTER);
        }

        @Override
        public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

        }

        @Override
        public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

        }

        @Override
        public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {
            SQLQuery.Select selection = TransferUtils.createTransferSelection(groupId, deviceId);

            try {
                TransferGroup group = new TransferGroup(groupId);

                database.reconstruct(dbInstance, group);
                database.removeAsObject(dbInstance, selection, TransferObject.class, null, group);
            } catch (Exception e) {
                database.remove(selection);
            }
        }
    }

    @Override
    public String toString() {
        return "TransferGroup{" +
                "groupId=" + groupId +
                ", dateCreated=" + dateCreated +
                ", savePath='" + savePath + '\'' +
                ", isServedOnWeb=" + isServedOnWeb +
                ", mIsSelected=" + mIsSelected +
                '}';
    }
}
