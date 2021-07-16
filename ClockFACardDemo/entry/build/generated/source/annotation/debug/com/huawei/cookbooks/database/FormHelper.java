// Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
package com.huawei.cookbooks.database;

import java.util.List;
import ohos.data.orm.EntityHelper;
import ohos.data.orm.OrmPredicates;
import ohos.data.rdb.Statement;
import ohos.data.resultset.ResultSet;

/**
 * Generated by ohos.data.orm.processor for form in com.huawei.cookbooks.database.FormDatabase. Do not modify!
 *
 * @author Huawei
 * @since 2021-07-15
 */
public class FormHelper implements EntityHelper<Form> {
    private static final FormHelper INSTANCE = new FormHelper();

    private static final String INSERT_STATEMENT = "INSERT INTO `form` (`formId`, `formName`, `dimension`) VALUES (?,?,?)";

    private static final String UPDATE_STATEMENT = "UPDATE `form` Set `formId` = ?, `formName` = ?, `dimension` = ? WHERE `rowid` = ?";

    private static final String DELETE_STATEMENT = "DELETE FROM `form` WHERE `rowid` = ?";

    private FormHelper() {
    }

    public static FormHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void bindValue(Statement statement, Form object) {
        Long formId = object.getFormId();
        if (formId != null) {
            statement.setLong(1, formId);
        } else {
            statement.setNull(1);
        }
        String formName = object.getFormName();
        if (formName != null) {
            statement.setString(2, formName);
        } else {
            statement.setNull(2);
        }
        Integer dimension = object.getDimension();
        if (dimension != null) {
            statement.setLong(3, dimension);
        } else {
            statement.setNull(3);
        }
    }

    @Override
    public void bindValue(Statement statement, Form object, long id) {
        this.bindValue(statement, object);
        statement.setLong(4, id);
    }

    @Override
    public Form createInstance(ResultSet resultSet) {
        Form object = new Form();
        object.setRowId(resultSet.getLong(0));
        object.setFormId(resultSet.isColumnNull(1) ? null : resultSet.getLong(1));
        object.setFormName(resultSet.getString(2));
        object.setDimension(resultSet.isColumnNull(3) ? null : resultSet.getInt(3));
        return object;
    }

    @Override
    public void setPrimaryKeyValue(Form object, long value) {
        object.setFormId(value);
    }

    @Override
    public String getTableName() {
        return "form";
    }

    @Override
    public String getInsertStatement() {
        return INSERT_STATEMENT;
    }

    @Override
    public String getUpdateStatement() {
        return UPDATE_STATEMENT;
    }

    @Override
    public String getDeleteStatement() {
        return DELETE_STATEMENT;
    }

    public OrmPredicates generatePredicatesGetRelated(String foreignKeyName, List<Form> objects) {
        return null;
    }
}
