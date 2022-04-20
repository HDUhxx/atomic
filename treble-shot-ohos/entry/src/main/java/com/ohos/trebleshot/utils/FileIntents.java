package com.ohos.trebleshot.utils;


import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;

public final class FileIntents {
    private final Context context;
    private Intent intent;

    private FileIntents(Context context)
    {
        this.context = context;
    }

    public static FileIntents from(Context context)
    {
        return new FileIntents(context);
    }

    public FileIntents fileChooser()
    {
        intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addEntity("android.intent.category.OPENABLE");
        intent.setParam("android.intent.extra.LOCAL_ONLY", true);
        return this;
    }

    public FileIntents pickFile()
    {
        intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("file/*");
        return this;
    }

    public FileIntents pickImageFile()
    {
        intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        return this;
    }

    // TODO: overload for other file types
    public FileIntents pickImageFile(Boolean allowMultiple, Boolean localOnly)
    {
        intent = new Intent();
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.addEntity("android.intent.category.OPENABLE");
        intent.setParam("android.intent.extra.ALLOW_MULTIPLE", allowMultiple);
        intent.setParam("android.intent.extra.LOCAL_ONLY", localOnly);
        return this;
    }

    public Intent build()
    {
        return intent;
    }

    private void startAbility(Intent intent)
    {
        if (!(context instanceof Ability))
        {
            intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
        }
        context.startAbility(intent,0);
    }

    public void show()
    {
        startAbility(build());
    }
}
