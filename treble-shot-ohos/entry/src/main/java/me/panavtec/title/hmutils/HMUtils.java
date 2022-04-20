package me.panavtec.title.hmutils;

import com.ohos.trebleshot.service.CommunicationService;
import com.ohos.trebleshot.service.RestartService;
import com.ohos.trebleshot.service.WorkerService;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.AbilityContext;
import ohos.app.Context;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.net.NetManager;
import ohos.rpc.RemoteException;
import ohos.utils.net.Uri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HMUtils {

    public static IntentAgent createIntentAgent(Context context, Intent intent, String abilityName, String action,  IntentAgentConstant.OperationType opType){
        // 指定要启动的Ability的BundleName和AbilityName字段
        // 将Operation对象设置到Intent中
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityName)
                .withAction(action)
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        // 定义请求码
        int requestCode = 200;
        // 设置flags
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG);
        // 指定启动一个有页面的Ability
        IntentAgentInfo paramsInfo = new IntentAgentInfo(requestCode, IntentAgentConstant.OperationType.START_SERVICE, flags, intentList, null);
        // 获取IntentAgent实例

        return IntentAgentHelper.getIntentAgent(context, paramsInfo);
    }

    public static IntentAgent createIntentAgent(Context context, Intent intent, String abilityName){
        // 指定要启动的Ability的BundleName和AbilityName字段
        // 将Operation对象设置到Intent中
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityName)
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        // 定义请求码
        int requestCode = 200;
        // 设置flags
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG);
        // 指定启动一个有页面的Ability
        IntentAgentInfo paramsInfo = new IntentAgentInfo(requestCode, IntentAgentConstant.OperationType.START_SERVICE, flags, intentList, null);
        // 获取IntentAgent实例

        return IntentAgentHelper.getIntentAgent(context, paramsInfo);
    }

    public static IntentAgent createIntentAgentWithUri(Context context, Intent intent, IntentAgentConstant.OperationType opType, Uri uri){
        // 指定要启动的Ability的BundleName和AbilityName字段
        // 将Operation对象设置到Intent中
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withUri(uri)
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        // 定义请求码
        int requestCode = 200;
        // 设置flags
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG);
        // 指定启动一个有页面的Ability
        IntentAgentInfo paramsInfo = new IntentAgentInfo(requestCode, IntentAgentConstant.OperationType.START_SERVICE, flags, intentList, null);
        // 获取IntentAgent实例

        return IntentAgentHelper.getIntentAgent(context, paramsInfo);
    }


    public static String transferLongToDate(String dateFormat,Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }

    public static void sendEvent(Intent intent , String action) throws RemoteException {
        Operation operation = new Intent.OperationBuilder()
                .withAction(action)
                .build();
        intent.setOperation(operation);
        CommonEventData eventData = new CommonEventData(intent);
        CommonEventManager.publishCommonEvent(eventData);
    }


    public static void startAbility(Context context, String abilityName, String action) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityName)
                .withAction(action)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);
    }

    public static void startAbilityForResult(AbilityContext context, Intent intent , String abilityName, String action, int requestCode) {
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityName)
                .withAction(action)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent, requestCode);
    }



    public static void startAbility(Context context, Intent intent, String abilityName, String action) {
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityName)
                .withAction(action)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);
    }

    public static void startService(Context context, Class abilityClazz, String action){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(abilityClazz)
                .withAction(action)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent,0);
    }


    public static void asyncRun(Runnable task, Runnable uiThreadRun){
        new EventHandler(EventRunner.create(true)).postTask(new Runnable() {
            @Override
            public void run() {
                task.run();
                new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                    @Override
                    public void run() {
                        uiThreadRun.run();
                    }
                });
            }
        });
    }


    public static void startService(Context context, boolean start){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(CommunicationService.class)
                .withAction(CommunicationService.ACTION_TOGGLE_WEBSHARE)
                .build();
        intent.setOperation(operation);
        if(start)
            context.startAbility(intent, 0);
        context.stopAbility(intent);
    }

    public static void startWorkerService(Context context, boolean start){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(WorkerService.class)
                .build();
        intent.setOperation(operation);
        if(start)
            context.startAbility(intent, 0);
        context.stopAbility(intent);
    }

    public static void restartApp(Context context) {
        startService(context, false);
        startWorkerService(context, false);

        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(context.getBundleName())
                .withAbilityName(RestartService.class)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);

        System.exit(0);
    }
    public static boolean isNetWorkAvailable(Context context){
        NetManager netManager = NetManager.getInstance(context);

        return netManager.hasDefaultNet();
    }
}
