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

import ohos.data.orm.OrmDatabase;
import ohos.data.orm.annotation.Database;

/**
 * Card Database
 */
@Database(
        entities = {Form.class},
        version = 1)
/*
* 代表对象数据库。
* 对象关系映射(ORM)数据库类对应于关系数据库。
*  在使用ORM数据库之前，您需要创建一个从OrmDatabase继承的数据库类，并使用@Database对其进行注释。
 */
public abstract class FormDatabase extends OrmDatabase { }
