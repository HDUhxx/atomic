package com.example.rdb.slice;

import com.example.rdb.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

public class MainAbilitySlice extends AbilitySlice {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    private static final String BASE_URI = "dataability:///com.example.rdb.Database.fristRDB";
    private static final String DATA_PATH = "/person";
    private static final String DB_COLUMN_PERSON_ID = "id";
    private static final String DB_COLUMN_NAME = "name";
    private static final String DB_COLUMN_GENDER = "gender";
    private static final String DB_COLUMN_AGE = "age";
    private DataAbilityHelper databaseHelper;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        databaseHelper = DataAbilityHelper.creator(this);

        insert(33, "Tom", "man", 20);
        query();
        delete(33);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void query() {
        String[] columns = new String[] {DB_COLUMN_PERSON_ID,
                DB_COLUMN_NAME, DB_COLUMN_GENDER, DB_COLUMN_AGE};
        // 构造查询条件
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.between(DB_COLUMN_AGE, 15, 40);
        try {
            ResultSet resultSet = databaseHelper.query(Uri.parse(BASE_URI + DATA_PATH),
                    columns, predicates);
            if (resultSet == null || resultSet.getRowCount() == 0) {
                HiLog.info(LABEL_LOG, "query: resultSet is null or no result found");
                return;
            }
            //移动到第一行
            resultSet.goToFirstRow();
            do {
                int id = resultSet.getInt(resultSet.getColumnIndexForName(DB_COLUMN_PERSON_ID));
                String name = resultSet.getString(resultSet.getColumnIndexForName(DB_COLUMN_NAME));
                String gender = resultSet.getString(resultSet.getColumnIndexForName(DB_COLUMN_GENDER));
                int age = resultSet.getInt(resultSet.getColumnIndexForName(DB_COLUMN_AGE));
                HiLog.info(LABEL_LOG, "query: Id :" + id + " Name :" + name + " Gender :" + gender + " Age :" + age);
            } while (resultSet.goToNextRow());//移动到下一行一行
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "query: dataRemote exception | illegalStateException");
        }
    }

    public void insert(int id, String name, String gender, int age) {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putInteger(DB_COLUMN_PERSON_ID, id);
        valuesBucket.putString(DB_COLUMN_NAME, name);
        valuesBucket.putString(DB_COLUMN_GENDER, gender);
        valuesBucket.putInteger(DB_COLUMN_AGE, age);
        HiLog.info(LABEL_LOG, "valuesBucket:"+valuesBucket);
        try {
            if (databaseHelper.insert(Uri.parse(BASE_URI + DATA_PATH), valuesBucket) != -1) {
                HiLog.info(LABEL_LOG, "insert successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "insert: dataRemote exception|illegalStateException");
        }
    }

    public void update() {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo(DB_COLUMN_PERSON_ID, 102);
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(DB_COLUMN_NAME, "ZhangSanPlus");
        valuesBucket.putInteger(DB_COLUMN_AGE, 28);
        try {
            if (databaseHelper.update(Uri.parse(BASE_URI + DATA_PATH), valuesBucket, predicates) != -1) {
                HiLog.info(LABEL_LOG, "update successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "update: dataRemote exception | illegalStateException");
        }
    }

    public void delete(int id) {
        DataAbilityPredicates predicates = new DataAbilityPredicates()
                .equalTo(DB_COLUMN_PERSON_ID, id);
        try {
            if (databaseHelper.delete(Uri.parse(BASE_URI + DATA_PATH), predicates) != -1) {
                HiLog.info(LABEL_LOG, "delete successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "delete: dataRemote exception | illegalStateException");
        }
    }
}
