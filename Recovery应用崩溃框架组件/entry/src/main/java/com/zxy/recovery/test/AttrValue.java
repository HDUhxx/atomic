/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.zxy.recovery.test;

import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.element.Element;
import ohos.agp.utils.Color;




/**
 * 属性工具类
 *
 * @author:wjt
 * @since 2021-04-06
 */
public class AttrValue {
    /**
     * 私有构造函数
     */
    private AttrValue() {
    }

    /**
     * 得到属性值
     *
     * @param attrSet attrSet
     * @param key key
     * @param defValue defValue
     * @param <T> defValue
     * @return value
     */
    @SuppressWarnings("unchecked cast")
    public static <T> T get(AttrSet attrSet, String key, T defValue) {
        if (attrSet == null || !attrSet.getAttr(key).isPresent()) {
            return (T) defValue;
        }

        Attr attr = attrSet.getAttr(key).get();
        if (defValue instanceof String) {
            return (T) attr.getStringValue();
        } else if (defValue instanceof Long) {
            return (T) (Long) (attr.getLongValue());
        } else if (defValue instanceof Float) {
            return (T) (Float) (attr.getFloatValue());
        } else if (defValue instanceof Integer) {
            return (T) (Integer) (attr.getIntegerValue());
        } else if (defValue instanceof Boolean) {
            return (T) (Boolean) (attr.getBoolValue());
        } else if (defValue instanceof Color) {
            return (T) (attr.getColorValue());
        } else if (defValue instanceof Element) {
            return (T) (attr.getElement());
        } else {
            return (T) defValue;
        }
    }

    /**
     * 得到Element
     *
     * @param attrSet attrSet
     * @param key key
     * @return Element
     */
    public static Element getElement(AttrSet attrSet, String key) {
        Element element = null;
        if (attrSet.getAttr(key).isPresent()) {
            element = attrSet.getAttr(key).get().getElement();
        }
        return element;
    }

    /**
     * getLayout
     *
     * @param attrSet attrSet
     * @param key key
     * @param def def
     * @return Layout
     */
    public static int getLayout(AttrSet attrSet, String key, int def) {
        if (attrSet.getAttr(key).isPresent()) {
            int layoutId = def;
            String value = attrSet.getAttr(key).get().getStringValue();
            if (value != null) {
                String subLayoutId = value.substring(value.indexOf(":"));
                layoutId = Integer.parseInt(subLayoutId);
            }
            return layoutId;
        }
        return def;
    }

    /**
     * getDimension
     *
     * @param attrSet attrSet
     * @param key key
     * @param defDimensionValue defDimensionValue
     * @return value
     */
    public static int getDimension(AttrSet attrSet, String key, int defDimensionValue) {
        if (!attrSet.getAttr(key).isPresent()) {
            return defDimensionValue;
        }

        Attr attr = attrSet.getAttr(key).get();
        return attr.getDimensionValue();
    }
}
