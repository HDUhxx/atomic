package com.ohos.trebleshot.object;

import com.ohos.trebleshot.database.*;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;

import java.util.List;

public class NetworkDevice
        implements DatabaseObject<Object> {
    public String brand;
    public String model;
    public String nickname;
    public String deviceId;
    public String versionName;
    public int versionNumber;
    public int tmpSecureKey;
    public long lastUsageTime;
    public boolean isTrusted = true;
    public boolean isRestricted = false;
    public boolean isLocalAddress = false;

    public NetworkDevice() {
    }

    public NetworkDevice(String deviceId) {
        this.deviceId = deviceId;
    }

    public NetworkDevice(CursorItem item) {
        reconstruct(item);
    }

    public String generatePictureId() {
        return String.format("picture_%s", deviceId);
    }

    @Override
    public SQLQuery.Select getWhere() {
        return new SQLQuery.Select(AccessDatabase.TABLE_DEVICES)
                .setWhere(AccessDatabase.FIELD_DEVICES_ID + "=?", deviceId);
    }

    public ValuesBucket getValues() {
        ValuesBucket values = new ValuesBucket();

        values.putString(AccessDatabase.FIELD_DEVICES_ID, deviceId);
        values.putString(AccessDatabase.FIELD_DEVICES_USER, nickname);
        values.putString(AccessDatabase.FIELD_DEVICES_BRAND, brand);
        values.putString(AccessDatabase.FIELD_DEVICES_MODEL, model);
        values.putString(AccessDatabase.FIELD_DEVICES_BUILDNAME, versionName);
        values.putInteger(AccessDatabase.FIELD_DEVICES_BUILDNUMBER, versionNumber);
        values.putLong(AccessDatabase.FIELD_DEVICES_LASTUSAGETIME, lastUsageTime);
        values.putInteger(AccessDatabase.FIELD_DEVICES_ISRESTRICTED, isRestricted ? 1 : 0);
        values.putInteger(AccessDatabase.FIELD_DEVICES_ISTRUSTED, isTrusted ? 1 : 0);
        values.putInteger(AccessDatabase.FIELD_DEVICES_ISLOCALADDRESS, isLocalAddress ? 1 : 0);
        values.putInteger(AccessDatabase.FIELD_DEVICES_TMPSECUREKEY, tmpSecureKey);

        return values;
    }

    @Override
    public void reconstruct(CursorItem item) {
        this.deviceId = item.getString(AccessDatabase.FIELD_DEVICES_ID);
        this.nickname = item.getString(AccessDatabase.FIELD_DEVICES_USER);
        this.brand = item.getString(AccessDatabase.FIELD_DEVICES_BRAND);
        this.model = item.getString(AccessDatabase.FIELD_DEVICES_MODEL);
        this.versionName = item.getString(AccessDatabase.FIELD_DEVICES_BUILDNAME);
        this.versionNumber = item.getInt(AccessDatabase.FIELD_DEVICES_BUILDNUMBER);
        this.lastUsageTime = item.getLong(AccessDatabase.FIELD_DEVICES_LASTUSAGETIME);
        this.isTrusted = item.getInt(AccessDatabase.FIELD_DEVICES_ISTRUSTED) == 1;
        this.isRestricted = item.getInt(AccessDatabase.FIELD_DEVICES_ISRESTRICTED) == 1;
        this.isLocalAddress = item.getInt(AccessDatabase.FIELD_DEVICES_ISLOCALADDRESS) == 1;
        this.tmpSecureKey = item.getInt(AccessDatabase.FIELD_DEVICES_TMPSECUREKEY);
    }

    @Override
    public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {

    }

    @Override
    public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {

    }

    @Override
    public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {
        database.getContext().deleteFile(generatePictureId());

        database.remove(dbInstance, new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION)
                .setWhere(AccessDatabase.FIELD_DEVICECONNECTION_DEVICEID + "=?", deviceId));

        List<TransferGroup.Assignee> assignees = database.castQuery(dbInstance, new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERASSIGNEE)
                .setWhere(AccessDatabase.FIELD_TRANSFERASSIGNEE_DEVICEID + "=?", deviceId), TransferGroup.Assignee.class, null);

        // We are ensuring that the transfer group is still valid for other devices
        for (TransferGroup.Assignee assignee : assignees) {
            database.remove(assignee);

            try {
                TransferGroup transferGroup = new TransferGroup(assignee.groupId);
                database.reconstruct(dbInstance, transferGroup);

                List<TransferGroup.Assignee> relatedAssignees = database.castQuery(new SQLQuery.Select(AccessDatabase.TABLE_TRANSFERASSIGNEE)
                        .setWhere(AccessDatabase.FIELD_TRANSFERASSIGNEE_GROUPID + "=?", String.valueOf(transferGroup.groupId)), TransferGroup.Assignee.class);

                if (relatedAssignees.size() == 0)
                    database.remove(transferGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Connection implements DatabaseObject<NetworkDevice> {
        public String adapterName;
        public String ipAddress;
        public String deviceId;
        public long lastCheckedDate;

        public Connection() {
        }

        public Connection(String adapterName, String ipAddress, String deviceId, long lastCheckedDate) {
            this.adapterName = adapterName;
            this.ipAddress = ipAddress;
            this.deviceId = deviceId;
            this.lastCheckedDate = lastCheckedDate;
        }

        public Connection(String deviceId, String adapterName) {
            this.deviceId = deviceId;
            this.adapterName = adapterName;
        }

        public Connection(TransferGroup.Assignee assignee) {
            this(assignee.deviceId, assignee.connectionAdapter);
        }

        public Connection(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public Connection(CursorItem item) {
            reconstruct(item);
        }

        @Override
        public SQLQuery.Select getWhere() {
            SQLQuery.Select select = new SQLQuery.Select(AccessDatabase.TABLE_DEVICECONNECTION);

            return ipAddress == null
                    ? select.setWhere(AccessDatabase.FIELD_DEVICECONNECTION_DEVICEID + "=? AND "
                    + AccessDatabase.FIELD_DEVICECONNECTION_ADAPTERNAME + "=?", deviceId, adapterName)
                    : select.setWhere(AccessDatabase.FIELD_DEVICECONNECTION_IPADDRESS + "=?", ipAddress);
        }

        @Override
        public ValuesBucket getValues() {
            ValuesBucket values = new ValuesBucket();

            values.putString(AccessDatabase.FIELD_DEVICECONNECTION_DEVICEID, deviceId);
            values.putString(AccessDatabase.FIELD_DEVICECONNECTION_ADAPTERNAME, adapterName);
            values.putString(AccessDatabase.FIELD_DEVICECONNECTION_IPADDRESS, ipAddress);
            values.putLong(AccessDatabase.FIELD_DEVICECONNECTION_LASTCHECKEDDATE, lastCheckedDate);

            return values;
        }

        @Override
        public void reconstruct(CursorItem item) {
            this.adapterName = item.getString(AccessDatabase.FIELD_DEVICECONNECTION_ADAPTERNAME);
            this.ipAddress = item.getString(AccessDatabase.FIELD_DEVICECONNECTION_IPADDRESS);
            this.deviceId = item.getString(AccessDatabase.FIELD_DEVICECONNECTION_DEVICEID);
            this.lastCheckedDate = item.getLong(AccessDatabase.FIELD_DEVICECONNECTION_LASTCHECKEDDATE);
        }

        @Override
        public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

        }

        @Override
        public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

        }

        @Override
        public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, NetworkDevice parent) {

        }
    }

    @Override
    public String toString() {
        return "NetworkDevice{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", nickname='" + nickname + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionNumber=" + versionNumber +
                ", tmpSecureKey=" + tmpSecureKey +
                ", lastUsageTime=" + lastUsageTime +
                ", isTrusted=" + isTrusted +
                ", isRestricted=" + isRestricted +
                ", isLocalAddress=" + isLocalAddress +
                '}';
    }
}
