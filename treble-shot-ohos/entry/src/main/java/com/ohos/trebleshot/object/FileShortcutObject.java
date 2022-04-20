package com.ohos.trebleshot.object;

import com.ohos.trebleshot.database.*;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;
import ohos.utils.net.Uri;

public class FileShortcutObject implements DatabaseObject<Object>
{
    public String title;
    public Uri path;

    public FileShortcutObject()
    {
    }

    public FileShortcutObject(Uri path)
    {
        this.path = path;
    }

    public FileShortcutObject(String title, Uri path)
    {
        this(path);
        this.title = title;
    }

    @Override
    public SQLQuery.Select getWhere()
    {
        return new SQLQuery.Select(AccessDatabase.TABLE_FILEBOOKMARK).setWhere(
                String.format("%s = ?", AccessDatabase.FIELD_FILEBOOKMARK_PATH), path.toString());
    }

    @Override
    public ValuesBucket getValues()
    {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(AccessDatabase.FIELD_FILEBOOKMARK_TITLE, title);
        valuesBucket.putString(AccessDatabase.FIELD_FILEBOOKMARK_PATH, path.toString());

        return valuesBucket;
    }

    @Override
    public void reconstruct(CursorItem item)
    {
        this.title = item.getString(AccessDatabase.FIELD_FILEBOOKMARK_TITLE);
        this.path = Uri.parse(item.getString(AccessDatabase.FIELD_FILEBOOKMARK_PATH));
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

