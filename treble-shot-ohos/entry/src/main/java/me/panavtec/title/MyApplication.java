package me.panavtec.title;

import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;

public class MyApplication extends AbilityPackage {

    public static Context mContext;

    public MyApplication() {
        mContext = this;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
    }

    public Context getContxt(){
        return this.getAbilityPackageContext();
    }
}
