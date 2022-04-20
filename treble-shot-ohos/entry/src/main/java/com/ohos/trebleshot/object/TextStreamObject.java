package com.ohos.trebleshot.object;

import com.ohos.trebleshot.database.*;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

/**
 * created by: Veli
 * date: 30.12.2017 13:19
 */

public class TextStreamObject implements DatabaseObject<Object>
{
    public long id;
    public String friendlyName;
    public String fileName;
    public String mimeType;
    public Uri uri;
    public long date;
    public long size;
    public String text;

    public TextStreamObject()
    {
    }
    public TextStreamObject(long id)
    {
        this.id = id;
    }
    public TextStreamObject(long id, String text, long date)
    {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof TextStreamObject && ((TextStreamObject) obj).id == id;
    }

    @Override
    public SQLQuery.Select getWhere()
    {
        return new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD)
                .setWhere(AccessDatabase.FIELD_CLIPBOARD_ID + "=?", String.valueOf(id));
    }

    @Override
    public ValuesBucket getValues()
    {
        ValuesBucket values = new ValuesBucket();

        values.putLong(AccessDatabase.FIELD_CLIPBOARD_ID, id);
        values.putLong(AccessDatabase.FIELD_CLIPBOARD_TIME, date);
        values.putString(AccessDatabase.FIELD_CLIPBOARD_TEXT, text);

        return values;
    }

    @Override
    public void reconstruct(CursorItem item)
    {
        this.id = item.getLong(AccessDatabase.FIELD_CLIPBOARD_ID);
        this.text = item.getString(AccessDatabase.FIELD_CLIPBOARD_TEXT);
        this.date = item.getLong(AccessDatabase.FIELD_CLIPBOARD_TIME);
        this.mimeType = "text/plain";
        this.size = text.length();
        this.friendlyName = text;
        this.fileName = text;
    }

    @Override
    public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
    {

    }

    @Override
    public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
    {

    }

    @Override
    public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
    {

    }
}
