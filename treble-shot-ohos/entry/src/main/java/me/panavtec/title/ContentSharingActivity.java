package me.panavtec.title;

import me.panavtec.title.hmutils.Toast;
import me.panavtec.title.slice.ContentSharingActivitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

public class ContentSharingActivity extends FractionAbility {

    public static final int SHARE_CODE = 3;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ContentSharingActivitySlice.class.getName());
        initPermission();
    }


    private void initPermission() {
        //1、读取权限设置(项目配置和用户确认)
        String[] permission = {"ohos.permission.READ_USER_STORAGE"};
        requestPermissionsFromUser(permission, SHARE_CODE);
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SHARE_CODE:
                // 匹配requestPermissions的requestCode
                if (grantResults.length > 0 && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    // 权限被授予
                    // 注意：因时间差导致接口权限检查时有无权限，所以对那些因无权限而抛异常的接口进行异常捕获处理
                    if (permissionCall != null) permissionCall.onpermisiongrant();
                } else {
                    Toast.show(this, "媒体读写权限被拒绝");
                }
                break;
        }
    }

    private PermissionCall permissionCall;

    public interface PermissionCall {
        void onpermisiongrant();
    }

    public void setPermissionCall(PermissionCall permissionCall) {
        this.permissionCall = permissionCall;
    }
}
