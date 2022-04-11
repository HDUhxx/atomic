package com.zxy.recovery.tools;

import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

import java.util.Map;
import java.util.Set;

/**
 * 共享参数工具
 *
 * @author:wjt
 * @since 2021-05-06
 */
public class SharedPreferencesCompat {
    private static final String SHAREINFO = "SharedPreferences key can not be empty!";
    private SharedPreferencesCompat() {
        throw new SharedPreferencesException("Stub!");
    }

    /**
     * 得到共享参数实例
     *
     * @param context
     * @param sharedPrefsName
     * @return Preferences
     */
    private static Preferences getSharedPrefs(Context context, String sharedPrefsName) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        checkNotNull(context, "Context can not be null!");
        checkNotEmpty(sharedPrefsName, "SharedPreferences name can not be empty!");
        return databaseHelper.getPreferences(sharedPrefsName);
    }

    /**
     * 存放String数据
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @param key 键
     * @param value 值
     */
    public static void put(Context context, String sharedPrefsName, String key, String value) {
        checkNotEmpty(key, SHAREINFO);
        checkNotNull(value, "SharedPreferences value can not be null!");
        Preferences preferences = getSharedPrefs(context, sharedPrefsName);
        preferences.putString(key, value);
        preferences.flush();
    }

    /**
     * 获取String值
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @param key 键
     * @param defValue 值
     * @return 值
     */
    public static String get(Context context, String sharedPrefsName, String key, String defValue) {
        checkNotEmpty(key, SHAREINFO);
        return getSharedPrefs(context, sharedPrefsName).getString(key, defValue);
    }

    /**
     * 清除所有数据
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     */
    public static void clear(Context context, String sharedPrefsName) {
        getSharedPrefs(context, sharedPrefsName).clear();
        getSharedPrefs(context, sharedPrefsName).flush();
    }

    /**
     * 移除指定键的数据
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @param key key
     */
    public static void remove(Context context, String sharedPrefsName, String key) {
        checkNotEmpty(key, SHAREINFO);
        Preferences preferences = getSharedPrefs(context, sharedPrefsName);
        preferences.delete(key);
        preferences.flush();
    }

    /**
     * 获取指定所有的数据
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @return 数据集合
     */
    public static Map<String, ?> getAll(Context context, String sharedPrefsName) {
        checkNotNull(context, "Context can not be null!");
        return getSharedPrefs(context, sharedPrefsName).getAll();
    }

    /**
     * 金策是否包含此键的数据
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @param key key
     * @return 是否包含
     */
    public static boolean contains(Context context, String sharedPrefsName, String key) {
        checkNotEmpty(key, SHAREINFO);
        return getSharedPrefs(context, sharedPrefsName).hasKey(key);
    }

    /**
     * 以字符串集和格式获取首选项的值。
     *
     * @param context 上下文
     * @param sharedPrefsName 指定文件名称
     * @param key key
     * @param defValues 默认值
     * @return key集合
     */
    public static Set<String> getStringSet(Context context, String sharedPrefsName, String key, Set<String> defValues) {
        checkNotEmpty(key, SHAREINFO);
        return getSharedPrefs(context, sharedPrefsName).getStringSet(key, defValues);
    }

    /**
     * 构建器
     *
     * @param context
     * @param sharedPrefsName
     * @return Builder
     */
    public static Builder newBuilder(Context context, String sharedPrefsName) {
        return new Builder(context, sharedPrefsName);
    }

    /**
     * 检查不为空
     *
     * @param t
     * @param message
     * @param <T>
     * @throws SharedPreferencesException
     */
    private static <T> void checkNotNull(T t, String message) {
        if (t == null) {
            throw new SharedPreferencesException(String.valueOf(message));
        }
    }

    private static void checkNotEmpty(String name, String message) {
        if (TextTool.isNullOrEmpty(name)) {
            throw new SharedPreferencesException(String.valueOf(message));
        }
    }

    /**
     * 构建器
     *
     * @since 2021-04-06
     */
    public static final class Builder {
        private Preferences preferences;
        private Builder(Context context, String sharedPrefsName) {
            preferences = getSharedPrefs(context, sharedPrefsName);
        }

        /**
         * 存放值
         *
         * @param key
         * @param value
         * @return Builder
         */
        public Builder put(String key, String value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            checkNotNull(value, "SharedPreferences value can not be null!");
            preferences.putString(key, value);
            return this;
        }

        /**
         * 提交值
         */
        public void commit() {
            if (preferences != null) {
                preferences.flush();
            }
        }
    }

    /**
     * SharedPreferencesException
     *
     * @since 2021-04-06
     */
    private static final class SharedPreferencesException extends RuntimeException {
        SharedPreferencesException(String message, Throwable cause) {
            super(message, cause);
        }

        SharedPreferencesException(String message) {
            super(message);
        }
    }
}