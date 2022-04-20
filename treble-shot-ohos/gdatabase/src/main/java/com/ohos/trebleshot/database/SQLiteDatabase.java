package com.ohos.trebleshot.database;

import com.ohos.trebleshot.database.exception.ReconstructionFailedException;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.StoreConfig;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.*;

/**
 * Created by: veli
 * Date: 1/31/17 4:51 PM
 * @noinspection unchecked, unchecked, unchecked, unchecked, unchecked, unchecked, unchecked, unchecked
 */

public abstract class SQLiteDatabase {
    private final Context mContext;
    public final RdbStore mStore;
    HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x1156, "SQLiteDatabase");

    public void onCreate(RdbStore db) {
    }

    public void onUpgrade(RdbStore database, int old, int current) {
    }

    public SQLiteDatabase(Context context, String dbName, int version) {
        mContext = context;
        StoreConfig config = StoreConfig.newDefaultConfig(dbName);
        RdbOpenCallback callback = new RdbOpenCallback() {
            @Override
            public void onCreate(RdbStore store) {
                SQLiteDatabase.this.onCreate(store);
            }

            @Override
            public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
                SQLiteDatabase.this.onUpgrade(store, oldVersion, newVersion);
            }
        };

        DatabaseHelper helper = new DatabaseHelper(mContext);
        mStore = helper.getRdbStore(config, 1, callback, null);
    }


    public <T extends DatabaseObject> List<T> castQuery(SQLQuery.Select select, final Class<T> clazz) {
        return castQuery(select, clazz, null);
    }

    public <T extends DatabaseObject> List<T> castQuery(SQLQuery.Select select, final Class<T> clazz, CastQueryListener<T> listener) {
        return castQuery(mStore, select, clazz, listener);
    }

    public <T extends DatabaseObject> List<T> castQuery(RdbStore db, SQLQuery.Select select, final Class<T> clazz, CastQueryListener<T> listener) {
        List<T> returnedList = new ArrayList<>();
        List<CursorItem> itemList = getTable(db, select);

        try {
            for (CursorItem item : itemList) {
                T newClazz = clazz.newInstance();

                newClazz.reconstruct(item);

                if (listener != null)
                    listener.onObjectReconstructed(this, item, newClazz);

                returnedList.add(newClazz);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return returnedList;
    }

    public <T, V extends DatabaseObject<T>> Map<String, List<V>> explodePerTable(List<V> objects) {
        Map<String, List<V>> tables = new HashMap<>();

        for (V object : objects) {
            String tableName = object.getWhere().tableName;
            List<V> availTable = tables.get(tableName);

            if (availTable == null) {
                availTable = new ArrayList<>();
                tables.put(tableName, availTable);
            }

            availTable.add(object);
        }

        return tables;
    }

    public Context getContext() {
        return mContext;
    }

    public CursorItem getFirstFromTable(SQLQuery.Select select) {
        return getFirstFromTable(mStore, select);
    }

    public CursorItem getFirstFromTable(RdbStore db, SQLQuery.Select select) {
        List<CursorItem> list = getTable(db, select.setLimit(1));
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<CursorItem> getTable(SQLQuery.Select select) {
        return getTable(mStore, select);
    }

    public List<CursorItem> getTable(RdbStore db, SQLQuery.Select select) {
        List<CursorItem> list = new ArrayList<>();

        String sql = "select * from " + select.tableName;
        if (select.where != null)
            sql += " where " + select.where;
        if (select.groupBy != null)
            sql += " group by " + select.groupBy;
        if (select.orderBy != null)
            sql += " order by " + select.orderBy;
        if (select.limit != null)
            sql += " limit " + select.limit;
        //todo log
        System.out.println(sql);
        ResultSet resultSet = db.querySql(sql, select.whereArgs);
        resultSet.goToFirstRow();

        if (resultSet.getRowCount() > 0) {
            if (select.loadListener != null)
                select.loadListener.onOpen(this, resultSet);

            do {
                CursorItem item = new CursorItem();

                for (int i = 0; i < resultSet.getColumnCount(); i++)
                    item.put(resultSet.getColumnNameForIndex(i), resultSet.getString(i));

                if (select.loadListener != null)
                    select.loadListener.onLoad(this, resultSet, item);

                list.add(item);
            } while (resultSet.goToNextRow());
        }

        resultSet.close();

        return list;
    }

    public long insert(DatabaseObject object) {
        return insert(mStore, object, null);
    }

    public <T> long insert(RdbStore db, DatabaseObject<T> object, T parent) {
        object.onCreateObject(db, this, parent);
        return insert(db, object.getWhere().tableName, null, object.getValues());
    }

    public long insert(RdbStore db, String tableName, String nullColumnHack, ValuesBucket valuesBucket) {
        return db.insert(tableName, valuesBucket);
    }

    public <T extends DatabaseObject> void insert(List<T> objects) {
        insert(objects, null);
    }

    public <T extends DatabaseObject> void insert(List<T> objects, ProgressUpdater updater) {
        insert(mStore, objects, updater, null);
    }

    public <T, V extends DatabaseObject<T>> void insert(RdbStore store, List<V> objects, ProgressUpdater updater, T parent) {
        Map<String, List<V>> tables = explodePerTable(objects);
        boolean successful = false;

        store.beginTransaction();

        try {
            if (tables.size() > 0) {
                for (String tableName : tables.keySet()) {
                    List<String> baseKeys = new ArrayList<>();
                    List<V> databaseObjects = tables.get(tableName);
                    int indexPosition = 0;

                    if (databaseObjects != null) {
                        for (V thisObject : databaseObjects) {
                            ValuesBucket valuesBucket = thisObject.getValues();
                            //todo log
                            baseKeys.addAll(valuesBucket.getColumnSet());
                            for (String key : baseKeys) {
                                System.out.println(key + "----" + valuesBucket.getObject(key));
                            }
                            store.insert(tableName, valuesBucket);


                            if (updater != null)
                                updater.onProgressChange(objects.size(), indexPosition++);
                        }
                    }
                }
            }
            store.markAsCommit();
            successful = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            store.endTransaction();
            if (successful)
                for (V object : objects)
                    object.onCreateObject(store, this, parent);
        }

    }

    public void publish(DatabaseObject object) {
        publish(mStore, object, null);
    }

    public <T> void publish(RdbStore database, DatabaseObject<T> object, T parent) {
        if (getFirstFromTable(database, object.getWhere()) != null)
            update(database, object, parent);
        else
            insert(database, object, parent);
    }

    public <T extends DatabaseObject> boolean publish(List<T> objects) {
        return publish(objects, null);
    }

    public <T extends DatabaseObject> boolean publish(List<T> objects, ProgressUpdater updater) {
        return publish(mStore, objects, updater, null);
    }

    public <T, V extends DatabaseObject<T>> boolean publish(RdbStore store, List<V> objects, ProgressUpdater updater, T parent) {
        Map<String, List<V>> tables = explodePerTable(objects);

        if (tables.size() > 0) {
            try {
                for (String tableName : tables.keySet()) {
                    List<V> objectList = tables.get(tableName);

                    if (objectList != null) {
                        List<DatabaseObject<T>> updatingObjects = new ArrayList<>();
                        List<DatabaseObject<T>> insertingObjects = new ArrayList<>();
                        int existenceIterator = 0;

                        for (V currentObject : objectList) {
                            if (updater != null && !updater.onProgressState())
                                return false;

                            if (getFirstFromTable(currentObject.getWhere()) == null)
                                insertingObjects.add(currentObject);
                            else
                                updatingObjects.add(currentObject);

                            if (updater != null)
                                updater.onProgressChange(objectList.size(), existenceIterator++);
                        }
                        insert(store, insertingObjects, updater, parent);
                        update(store, updatingObjects, updater, parent);


                    }
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void reconstruct(DatabaseObject object) throws ReconstructionFailedException {
        reconstruct(mStore, object);
    }

    public void reconstruct(RdbStore db, DatabaseObject object) throws ReconstructionFailedException {
        CursorItem item = getFirstFromTable(db, object.getWhere());
        if (item == null) {
            SQLQuery.Select select = object.getWhere();

            StringBuilder whereArgs = new StringBuilder();

            for (String arg : select.whereArgs) {
                if (whereArgs.length() > 0)
                    whereArgs.append(", ");

                whereArgs.append("[] ");
                whereArgs.append(arg);
            }

            throw new ReconstructionFailedException("No data was returned from: query"
                    + "; tableName: " + select.tableName
                    + "; where: " + select.where
                    + "; whereArgs: " + whereArgs.toString());
        }

        object.reconstruct(item);
    }

    public void remove(DatabaseObject object) {
        remove(mStore, object, null);
    }

    public <T> void remove(RdbStore db, DatabaseObject<T> object, T parent) {
        object.onRemoveObject(db, this, parent);
        remove(db, object.getWhere());
    }

    public int remove(SQLQuery.Select select) {
        return remove(mStore, select);
    }

    public int remove(RdbStore db, SQLQuery.Select select) {
        String sql = "delete from " + select.tableName;
        if (select.where != null)
            sql += " where " + select.where;
        db.executeSql(sql, select.whereArgs);
        return 0;// db.delete(select.tableName, select.where, select.whereArgs);
    }

    public <T extends DatabaseObject> void remove(List<T> objects) {
        remove(objects, null);
    }

    public <T extends DatabaseObject> void remove(List<T> objects, ProgressUpdater updater) {
        remove(mStore, objects, updater, null);
    }

    public <T, V extends DatabaseObject<T>> void remove(RdbStore store, List<V> objects, ProgressUpdater updater, T parent) {
        int progress = 0;
        boolean successful = false;

        store.beginTransaction();

        try {
            for (V object : objects) {
                if (updater != null && !updater.onProgressState())
                    break;
                remove(object);
                if (updater != null)
                    updater.onProgressChange(objects.size(), progress++);
            }
            store.markAsCommit();
            successful = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            store.endTransaction();

            if (successful)
                for (V object : objects)
                    object.onRemoveObject(store, this, parent);
        }
    }

    public <V, T extends DatabaseObject<V>> void removeAsObject(RdbStore database,
                                                                SQLQuery.Select select,
                                                                Class<T> objectType,
                                                                CastQueryListener<T> listener,
                                                                V parent) {
        List<T> transferList = castQuery(database, select, objectType, listener);

        remove(database, select);

        for (T object : transferList)
            object.onRemoveObject(database, this, parent);
    }

    public int update(DatabaseObject object) {
        return update(mStore, object, null);
    }

    public <T> int update(RdbStore db, DatabaseObject<T> object, T parent) {
        object.onUpdateObject(db, this, parent);
        return update(db, object.getWhere(), object.getValues());
    }

    public int update(SQLQuery.Select select, ValuesBucket values) {
        return update(mStore, select, values);
    }

    public int update(RdbStore database, SQLQuery.Select select, ValuesBucket values) {
        String sql = "update " + select.tableName + " set ";
        Set<String> cols = values.getColumnSet();
        List<Object> datas = new ArrayList<>();
        for (String col : cols) {
            datas.add(values.getObject(col));
            sql += " " + col + "=? ,";
        }
        sql = sql.substring(0, sql.length() - 1);
        if (select.where != null)
            sql += " where " + select.where;
        datas.addAll(Arrays.asList(select.whereArgs));
        Object[] args = new Object[datas.size()];
        args = datas.toArray(args);
        database.executeSql(sql, args);
        return 0;
    }

    public <T extends DatabaseObject> void update(List<T> objects) {
        update(objects, null);
    }

    public <T extends DatabaseObject> void update(List<T> objects, ProgressUpdater updater) {
        update(mStore, objects, updater, null);
    }

    public <T, V extends DatabaseObject<T>> void update(RdbStore store, List<V> objects, ProgressUpdater updater, T parent) {
        int progress = 0;
        boolean successful = false;

        store.beginTransaction();

        try {
            for (V object : objects) {
                if (updater != null && !updater.onProgressState())
                    break;

                update(object);

                if (updater != null)
                    updater.onProgressChange(objects.size(), progress++);
            }
            store.markAsCommit();
            successful = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            store.endTransaction();

            if (successful)
                for (V object : objects)
                    object.onUpdateObject(store, this, parent);
        }
    }

    public interface CastQueryListener<T extends DatabaseObject> {
        void onObjectReconstructed(SQLiteDatabase db, CursorItem item, T object);
    }

    public interface ProgressUpdater {
        void onProgressChange(int total, int current);

        boolean onProgressState(); // true to continue
    }
}