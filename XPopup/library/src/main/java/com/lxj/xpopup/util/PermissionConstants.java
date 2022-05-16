package com.lxj.xpopup.util;

import ohos.security.SystemPermission;

/**
 * Create by dance, at 2019/4/1
 */
public final class PermissionConstants {

    /**
     * 读取SD卡
     */
    public static final String READ_USER_STORAGE = SystemPermission.READ_USER_STORAGE; // 读取SD卡
    /**
     * 写入SD卡
     */
    public static final String WRITE_USER_STORAGE = SystemPermission.WRITE_USER_STORAGE;
    /**
     * 允许应用读取用户外部存储中的媒体文件信息
     */
    public static final String READ_MEDIA = SystemPermission.READ_MEDIA;
    /**
     * 允许应用读写用户外部存储中的媒体文件信息
     */
    public static final String WRITE_MEDIA = SystemPermission.WRITE_MEDIA;
    /**
     * 照相
     */
    public static final String CAMERA = SystemPermission.CAMERA;
    /**
     * 允许应用获取位置信息
     */
    public static final String LOCATION = SystemPermission.LOCATION;
    /**
     * 允许应用在后台运行时获取位置信息
     */
    public static final String LOCATION_IN_BACKGROUND = SystemPermission.LOCATION_IN_BACKGROUND;

}