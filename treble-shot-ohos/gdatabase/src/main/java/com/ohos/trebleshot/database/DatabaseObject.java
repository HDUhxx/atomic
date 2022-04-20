package com.ohos.trebleshot.database;

import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;

/**
 * created by: Veli
 * date: 2.11.2017 21:31
 */

public interface DatabaseObject<T> {
    SQLQuery.Select getWhere();

    ValuesBucket getValues();

    void reconstruct(CursorItem item);

    void onCreateObject(RdbStore db, SQLiteDatabase database, T parent);

    void onUpdateObject(RdbStore db, SQLiteDatabase database, T parent);

    void onRemoveObject(RdbStore db, SQLiteDatabase database, T parent);
}
