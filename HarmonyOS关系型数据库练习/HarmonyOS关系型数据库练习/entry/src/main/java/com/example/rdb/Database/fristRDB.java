package com.example.rdb.Database;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.FileDescriptor;

public class fristRDB extends Ability {
    //    数据库参数
    private static final String DB_NAME = "persondataability.db";       //数据库名称
    private static final String DB_TAB_NAME = "person";                 //表名称
    private static final String DB_COLUMN_PERSON_ID = "id";             //列 id
    private static final String DB_COLUMN_NAME = "name";                //列 name
    private static final String DB_COLUMN_GENDER = "gender";            //列 gender
    private static final String DB_COLUMN_AGE = "age";                  //列 age
    private static final int DB_VERSION = 1;                            //版本

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");


    private StoreConfig config = StoreConfig.newDefaultConfig(DB_NAME);  //通过指定数据库名称使用默认配置创建数据库配置。
    private RdbStore rdbStore;  //提供管理关系数据库 (RDB) 的方法。
    //   RdbOpenCallback 管理数据库创建、升级和降级。
    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {  // 数据库创建时被回调，开发者可以在该方法中初始化表结构，并添加一些应用使用到的初始化数据。
            store.executeSql("create table if not exists "
                    + DB_TAB_NAME + " ("
                    + DB_COLUMN_PERSON_ID + " integer primary key, "
                    + DB_COLUMN_NAME + " text not null, "
                    + DB_COLUMN_GENDER + " text not null, "
                    + DB_COLUMN_AGE + " integer)");
        }

        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "fristRDB onStart");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //   根据配置创建或打开数据库。
        rdbStore = databaseHelper.getRdbStore(config, DB_VERSION, rdbOpenCallback, null);
    }

    /**
     * 数据查询
     * @param uri
     * @param columns
     * @param predicates
     * @return
     */
    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        //   拼装查询语句
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        //   查询表中数据 根据 查询语句 + 查找的列
        ResultSet resultSet = rdbStore.query(rdbPredicates, columns);
        if (resultSet == null) {
            HiLog.info(LABEL_LOG, "resultSet is null");
        }
        return resultSet;
    }

    /**
     * 数据添加
     * @param uri  插入的目标路径
     * @param value  插入的数据值
     * @return
     */
    @Override
    public int insert(Uri uri, ValuesBucket value) {
        HiLog.info(LABEL_LOG, "fristRDB insert");

        String path = uri.getLastPath();
        // 判断数据添加的表名称
        if (!"person".equals(path)) {
            HiLog.info(LABEL_LOG, "DataAbility insert path is not matched");
            return -1;
        }
        //用于整理存储添加的数据
        ValuesBucket values = new ValuesBucket();
        values.putInteger(DB_COLUMN_PERSON_ID, value.getInteger(DB_COLUMN_PERSON_ID));
        values.putString(DB_COLUMN_NAME, value.getString(DB_COLUMN_NAME));
        values.putString(DB_COLUMN_GENDER, value.getString(DB_COLUMN_GENDER));
        values.putInteger(DB_COLUMN_AGE, value.getInteger(DB_COLUMN_AGE));
        int index = (int) rdbStore.insert(DB_TAB_NAME, values);
        //  当表格数据插入成功时，可执行DataAbilityHelper.creator(this, uri).notifyChange(uri)，通知该表格数据的订阅者
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    /**
     *  数据删除
     * @param uri  删除的目标路径
     * @param predicates  删除条件
     * @return
     */
    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        //   解析出要删除的数据
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.delete(rdbPredicates);
        HiLog.info(LABEL_LOG, "delete: " + index);
        //   通知观察者数据已经修改
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    /**
     * 数据修改
     * @param uri
     * @param value
     * @param predicates
     * @return
     */
    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        //   解析出要修改的数据
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.update(value, rdbPredicates);
        HiLog.info(LABEL_LOG, "update: " + index);
        //   通知观察者数据已经修改
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        return null;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public void myInsert(){
        ValuesBucket values = new ValuesBucket();
        values.putInteger("id", 1);
        values.putString("name", "zhangsan");
        values.putInteger("age", 18);
        long id = rdbStore.insert("person", values);
    }
}