package com.ohos.trebleshot.object;

import com.ohos.trebleshot.database.*;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

/**
 * created by: Veli
 * date: 16.02.2018 12:56
 */

public class WritablePathObject implements DatabaseObject<Object> {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    private String title;
    private Uri path;

    public WritablePathObject() {
    }

    public WritablePathObject(Uri path) {
        this.path = path;
    }

    public WritablePathObject(String title, Uri path) {
        this(path);
        this.title = title;
    }

    @Override
    public SQLQuery.Select getWhere() {
        return new SQLQuery.Select(AccessDatabase.TABLE_WRITABLEPATH)
                .setWhere(AccessDatabase.FIELD_WRITABLEPATH_PATH + "=?", path.toString());
    }

    @Override
    public ValuesBucket getValues() {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(AccessDatabase.FIELD_WRITABLEPATH_TITLE, title);
        valuesBucket.putString(AccessDatabase.FIELD_WRITABLEPATH_PATH, path.toString());

        return valuesBucket;
    }

    @Override
    public void reconstruct(CursorItem item) {
        this.title = item.getString(AccessDatabase.FIELD_WRITABLEPATH_TITLE);
        this.path = Uri.parse(item.getString(AccessDatabase.FIELD_WRITABLEPATH_PATH));
    }

    @Override
    public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {

    }

    @Override
    public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {

    }

    @Override
    public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, Object parent) {

    }
}
