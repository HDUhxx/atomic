/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.cookbooks.database;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;

/**
 * Card Table
 * 表示对象关系映射(ORM)数据库中的实体。
 * 关系数据库(RDB)表中的一行对应于ORM数据库中的一个实体。 在操作ORM数据库中的实体之前，您需要创建一个从OrmObject继承的实体类，并使用@Entity对其进行注释。
 */
@Entity(tableName = "form")
public class Form extends OrmObject {
    @PrimaryKey()
    private Long formId;
    private String formName;
    private Integer dimension;

    public Form(Long formId, String formName, Integer dimension) {
        this.formId = formId;
        this.formName = formName;
        this.dimension = dimension;
    }

    public Form() { }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
