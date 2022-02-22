/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.util;

import java.lang.reflect.Field;

/**
 * fuqiangping
 * 反射工具类
 *
 * @since 2021-04-29
 */
public class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 获取类里指定的变量
     *
     * @param thisClass
     * @param fieldName
     * @return Field
     */
    public static Field getField(Class<?> thisClass, String fieldName) {
        Field field = null;
        if (thisClass == null) {
            return field;
        }

        try {
            field = thisClass.getDeclaredField(fieldName);
        } catch (Exception e) {
            e.getMessage();
        }
        return field;
    }

    /**
     * 获取对象里变量的值
     *
     * @param instance
     * @param fieldName
     * @return 返回空则可能值不存在，或变量不存在
     */
    public static Object getValue(Object instance, String fieldName) {
        Field field = getField(instance.getClass(), fieldName);
        if (field == null) {
            return field;
        }

        // 参数值为true，禁用访问控制检查
        field.setAccessible(true);
        try {
            return field.get(instance);
        } catch (Throwable e) {
            return field;
        }
    }

    /**
     * 获取静态变量的值
     *
     * @param clazz
     * @param fieldName
     * @return 返回空则可能值不存在，或变量不存在
     */
    public static Object getValue(Class clazz, String fieldName) {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            return field;
        }

        // 参数值为true，禁用访问控制检查
        field.setAccessible(true);
        try {
            return field.get(null);
        } catch (Throwable e) {
            return field;
        }
    }

    /**
     * 获取类里的方法
     *
     * @param thisClass
     * @param methodName
     * @param parameterTypes
     * @return Method
     */
/*    public static Method getMethod(Class<?> thisClass, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        if (thisClass == null) {
            return method;
        }
        try {
            method = thisClass.getDeclaredMethod(methodName, parameterTypes);
            if (method == null) {
                return method;
            }
            method.setAccessible(true);
            return method;
        } catch (Throwable e) {
            return method;
        }
    }*/

    /**
     * 执行对象里的方法
     *
     * @param instance
     * @param methodName
     * @param args 方法参数
     * @return 返回值
     * @throws Throwable 方法不存在或者执行失败跑出异常
     */
/*    public static Object invokeMethod(Object instance, String methodName, Object... args) throws Throwable {
        Class<?>[] parameterTypes = null;
        if (args != null) {
            parameterTypes = new Class[args.length];
            for (int index = 0; index < args.length; index++) {
                if (args[index] != null) {
                    parameterTypes[index] = args[index].getClass();
                }
            }
        }
        Method method = getMethod(instance.getClass(), methodName, parameterTypes);
        return method.invoke(instance, args);
    }*/

    /**
     * 执行静态方法
     *
     * @param clazz
     * @param methodName
     * @param args 方法参数
     * @return 返回值
     * @throws Throwable 方法不存在或者执行失败跑出异常
     */
/*    public static Object invokeMethod(Class clazz, String methodName, Object... args) throws Throwable {
        Class<?>[] parameterTypes = null;
        if (args != null) {
            parameterTypes = new Class[args.length];
            for (int index = 0; index < args.length; index++) {
                if (args[index] != null) {
                    parameterTypes[index] = args[index].getClass();
                }
            }
        }
        Method method = getMethod(clazz, methodName, parameterTypes);
        return method.invoke(clazz, args);
    }*/
}
