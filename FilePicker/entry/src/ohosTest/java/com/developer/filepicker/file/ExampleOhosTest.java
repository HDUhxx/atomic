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

package com.developer.filepicker.file;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * ExampleOhosTest
 *
 * @author ljx
 * @since 2021-07-012
 */
public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.developer.filepicker.file", actualBundleName);
    }

    @Test
    public void getStrength() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.FileListItem");
            Method log = fileListItem.getMethod("getFilename");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void getLocation() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.FileListItem");
            Method log = fileListItem.getMethod("getLocation");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void getTime() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.FileListItem");
            Method log = fileListItem.getMethod("getTime");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void isMarked() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.FileListItem");
            Method log = fileListItem.getMethod("isMarked");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void clearSelectionList() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.MarkedItemList");
            Method log = fileListItem.getMethod("clearSelectionList");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void getFileCount() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.model.MarkedItemList");
            Method log = fileListItem.getMethod("getFileCount");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void initView() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.widget.MaterialCheckbox");
            Method log = fileListItem.getMethod("initView");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void getName() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.file.ListItem");
            Method log = fileListItem.getMethod("getName");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }

    @Test
    public void getPath() {
        try {
            Class fileListItem = Class.forName("com.developer.filepicker.file.ListItem");
            Method log = fileListItem.getMethod("getPath");
            Object obj = fileListItem.getConstructor().newInstance();
            log.invoke(obj);
        } catch (ClassNotFoundException ep) {
            ep.printStackTrace();
        } catch (NoSuchMethodException ep) {
            ep.printStackTrace();
        } catch (IllegalAccessException ep) {
            ep.printStackTrace();
        } catch (InstantiationException ep) {
            ep.printStackTrace();
        } catch (InvocationTargetException ep) {
            ep.printStackTrace();
        }
    }
}