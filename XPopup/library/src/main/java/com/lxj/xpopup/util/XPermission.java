package com.lxj.xpopup.util;

import ohos.app.Context;
import ohos.bundle.IBundleManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by dance, at 2019/4/1
 */
public final class XPermission {

    private static XPermission sInstance;
    private static WeakReference<Context> context;
    private static SimpleCallback mSimpleCallback;
    private static List<String> mPermissionsRequest;
    private static List<String> mPermissionsGranted;
    private static List<String> mPermissionsDenied;
    private static List<String> mPermissionsDeniedForever;

    /**
     * Set the permissions.
     *
     * @param ctx         context
     * @param permissions The permissions.
     * @return the single {@link XPermission} instance
     */
    public static XPermission create(Context ctx, final String... permissions) {
        if (sInstance == null) {
            return new XPermission(ctx, permissions);
        }
        sInstance.context = new WeakReference<>(ctx);
        sInstance.prepare(permissions);
        return sInstance;
    }

    public static XPermission getInstance() {
        return sInstance;
    }

    private XPermission(Context ctx, final String... permissions) {
        sInstance = this;
        context = new WeakReference<>(ctx);
        prepare(permissions);
    }

    private void prepare(final String... permissions) {
        mPermissionsRequest = new ArrayList<>();
        if (permissions == null) {
            return;
        }
        for (String permission : permissions) {
            mPermissionsRequest.add(permission);
        }
    }

    /**
     * Set the simple call back.
     *
     * @param callback the simple call back
     * @return XPermission对象
     */
    public static XPermission callback(final SimpleCallback callback) {
        mSimpleCallback = callback;
        return sInstance;
    }

    /**
     * Start request.
     */
    public static void request() {
        mPermissionsGranted = new ArrayList<>();
        mPermissionsDenied = new ArrayList<>();
        mPermissionsDeniedForever = new ArrayList<>();
        if (mPermissionsRequest.isEmpty()) {
            requestCallback();
        } else {
            int size = mPermissionsRequest.size();
            for (int i = 0; i < size; i++) {
                String permission = mPermissionsRequest.get(i);
                if (context.get().verifySelfPermission(permission) != IBundleManager.PERMISSION_GRANTED) { // 应用未被授予权限
                    // 权限未被授予
                    if (context.get().canRequestPermission(permission)) {
                        // 可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                        mPermissionsDenied.add(permission);
                    } else {
                        // 不可以申请弹框授权(用户已选择禁止且不再提示)
                        // 显示应用需要权限的理由，提示用户进入设置授权
                        mPermissionsDeniedForever.add(permission);
                    }
                } else {
                    // 权限已被授予
                    mPermissionsGranted.add(permission);
                }
            }
            if (!mPermissionsDeniedForever.isEmpty()) {
                mSimpleCallback.onForbid(mPermissionsDeniedForever);
                return;
            } else if (!mPermissionsDenied.isEmpty()) {
                context.get().requestPermissionsFromUser(XPermission.sInstance.mPermissionsDenied.toArray(new String[size]), 1);
                return;
            } else if (mPermissionsGranted.size() == mPermissionsRequest.size()) {
                mSimpleCallback.onGranted();
            }
        }
    }

    public static void onRequestPermissionsFromUserResult(Context mContext, int requestCode, String[] permissions, int[] grantResults) {
        if (context.get() == mContext) {
            if (requestCode == 1) {
                // 匹配requestPermissions的requestCode
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == IBundleManager.PERMISSION_DENIED) {
                        // 有权限被拒绝
                        XPermission.mSimpleCallback.onDenied();
                        return;
                    }
                }
                // 说明所有的权限都允许了
                XPermission.mSimpleCallback.onGranted();
                XPermission.sInstance.onDestroy();
            }
        }
    }

    private static void requestCallback() {
        if (mSimpleCallback != null) {
            if (mPermissionsRequest.size() == 0) {
                mSimpleCallback.onGranted();
            } else {
                mSimpleCallback.onDenied();
            }
            mSimpleCallback = null;
        }
    }

    public interface SimpleCallback {
        void onGranted();

        void onDenied();

        void onForbid(List<String> mPermissionsDeniedForever);
    }

    public void releaseConext() {
        context = null;
    }

    public static void onDestroy() {
        if (context != null) {
            context.clear();
            context = null;
        }
        if (mPermissionsRequest != null) {
            mPermissionsRequest.clear();
            mPermissionsRequest = null;
        }
        if (mPermissionsGranted != null) {
            mPermissionsGranted.clear();
            mPermissionsGranted = null;
        }
        if (mPermissionsDenied != null) {
            mPermissionsDenied.clear();
            mPermissionsDenied = null;
        }
        if (mPermissionsDeniedForever != null) {
            mPermissionsDeniedForever.clear();
            mPermissionsDeniedForever = null;
        }
        mSimpleCallback = null;
        sInstance = null;
    }


}