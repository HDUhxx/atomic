package me.panavtec.title;

import me.panavtec.title.hminterface.PermissionInterface;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.hmutils.HmAddConstant;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import me.panavtec.title.slice.MainFractionAbilitySlice;
import me.panavtec.title.slice.WelcomeAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.IBundleManager;

import java.io.File;

public class MainAbility extends FractionAbility {
    //    HmSharedPerfrence perfrence = new HmSharedPerfrence(this);
    final HmSharedPerfrence perfrence = HmSharedPerfrence.getInstance(this);
    PermissionInterface anInterface;
//    UIConnectionUtils connectionUtils = new UIConnectionUtils();

    public void setAnInterface(PermissionInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        if (!perfrence.isFrist(HmAddConstant.HM_SHAREPERENCE_NAME)) {
            super.setMainRoute(WelcomeAbilitySlice.class.getName());
        } else {
            super.setMainRoute(MainFractionAbilitySlice.class.getName());
        }
        doSaveSharePerferce();
        HMUtils.startService(this, true);
        HMUtils.startWorkerService(this, true);
    }

    //    ohos.aafwk.ability.Ability
    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 3: {
                // 匹配requestPermissions的requestCode
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    // 权限被授予
                    // 注意：因时间差导致接口权限检查时有无权限，所以对那些因无权限而抛异常的接口进行异常捕获处理
//                    new ToastDialog(this).setText("媒体读写权限被授予").setDuration(2000).back_header();
                    anInterface.isPermission(true);
                } else {
                    // 权限被拒绝
                    new ToastDialog(this).setText("媒体读写权限被拒绝").setDuration(2000).show();
                    anInterface.isPermission(false);
                }
            }
        }
    }

    public void doSaveSharePerferce() {

        if (perfrence.getParam("typ1") == null || perfrence.getParam("type1").length() < 1) {
//            new ToastDialog(this).setText("执行初始化操作").setDuration(2000).show();
            perfrence.saveParam("type1", "/mnt&&/data/data/com.loser007/MainAbility/preferences");
            System.out.println("执行初始化操作" + perfrence.getParam("type1"));
            perfrence.saveParam("type2", "/mnt/modem/modem_secure&&/mnt/user" + "&&" + creatSaveFile());
//            "/storage/emulated/0/DCIM/Camera/IMG_20210422_165248.jpg"
            perfrence.saveParam("type3", "/storage/emulated/0/DCIM/Camera&&/storage/emulated/0/Music&&/mnt/sdcard/DCIM/Camera");
        }
    }

    public String creatSaveFile() {
        File dir = new File("/data/data/com.loser007/files");
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

}
