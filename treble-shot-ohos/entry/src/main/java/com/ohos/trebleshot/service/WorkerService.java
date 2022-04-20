package com.ohos.trebleshot.service;

import com.ohos.trebleshot.utils.*;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.rpc.IRemoteObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ohos.trebleshot.utils.NotificationUtils.getNotificationUtilsInst;

/**
 * created by: Veli
 * date: 26.01.2018 14:21
 */

public class WorkerService extends Ability
{
    public static final String TAG = WorkerService.class.getSimpleName();

    public static final String ACTION_KILL_SIGNAL = "com.genonbeta.intent.action.KILL_SIGNAL";
    public static final String ACTION_KILL_ALL_SIGNAL = "com.genonbeta.intent.action.KILL_ALL_SIGNAL";
    public static final String EXTRA_TASK_HASH = "extraTaskId";

    public static final int REQUEST_CODE_RESCUE_TASK = 10910;
    public static final int ID_NOTIFICATION_FOREGROUND = 1103;

    private static WorkerService self;

    private final List<RunningTask> mTaskList = new ArrayList<>();
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private NotificationUtils mNotificationUtils;
    private DynamicNotification mNotification;

    public static int intentHash(Intent intent)
    {
        StringBuilder builder = new StringBuilder();

//        builder.append(intent.getComponent());
//        builder.append(intent.getData());
//        builder.append(intent.getPackage());
        builder.append(intent.getAction());
        builder.append(intent.getFlags());
        builder.append(intent.getType());

        if (intent.getParams() != null)
            builder.append(intent.getParams().toString());

        return builder.toString().hashCode();
    }

    public static WorkerService getSelf() {
        return self;
    }

    public static void setSelf(WorkerService self) {
        WorkerService.self = self;
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return super.onConnect(intent);
    }

    @Override
    public void onDisconnect(Intent intent) {
        super.onDisconnect(intent);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setSelf(this);
        mNotificationUtils = getNotificationUtilsInst(this);
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId)
    {
        super.onCommand(intent, restart, startId);

        if (intent != null)

            if (ACTION_KILL_SIGNAL.equals(intent.getAction()) && intent.hasParameter(EXTRA_TASK_HASH)) {
                int taskHash = intent.getIntParam(EXTRA_TASK_HASH, -1);

                RunningTask runningTask = findTaskByHash(taskHash);

                if (runningTask == null || runningTask.getInterrupter().interrupted())
                    mNotificationUtils.cancel(taskHash);
                else {
                    runningTask.getInterrupter().interrupt();
                    runningTask.onInterrupted();
                }
            } else if (ACTION_KILL_ALL_SIGNAL.equals(intent.getAction())) {
                synchronized (getTaskList()) {
                    for (RunningTask runningTask : getTaskList()) {
                        runningTask.getInterrupter().interrupt();
                        runningTask.onInterrupted();
                    }
                }
            }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        synchronized (getTaskList()) {
            for (RunningTask runningTask : getTaskList())
                runningTask.getInterrupter().interrupt(false);
        }

        cancelBackgroundRunning();
    }

    public synchronized RunningTask findTaskByHash(int hashCode)
    {
        synchronized (getTaskList()) {
            for (RunningTask runningTask : getTaskList())
                if (runningTask.hashCode() == hashCode)
                    return runningTask;
        }

        return null;
    }

    public List<RunningTask> getTaskList()
    {
        return mTaskList;
    }

    public void publishNotification(RunningTask runningTask)
    {
        if (runningTask.mNotification == null) {
            Intent intent = new Intent()
                    .setParam(EXTRA_TASK_HASH, runningTask.hashCode());

            IntentAgent cancelIntent =
                    HMUtils.createIntentAgent(this, intent, WorkerService.class.getName(), ACTION_KILL_SIGNAL, IntentAgentConstant.OperationType.START_SERVICE);


            String info = runningTask.mTitle == null ? getString(ResourceTable.String_text_taskOngoing) : runningTask.mTitle;
            runningTask.mNotification = mNotificationUtils.buildDynamicNotification(
                    runningTask.hashCode(), NotificationUtils.mNotificationChannelLow, info);

            runningTask.mNotification.setSmallIcon(runningTask.getIconRes() == 0
                    ? ResourceTable.Media_ic_autorenew_white_24dp_static
                    : runningTask.getIconRes());

//            runningTask.mNotification.setContentTitle(getString(ResourceTable.String_text_taskOngoing));
            runningTask.mNotification.addAction(ResourceTable.Media_ic_close_white_24dp_static,
                    getString(ResourceTable.String_butn_cancel), cancelIntent);

            if (runningTask.mIntentAgent != null)
                runningTask.mNotification.setContentIntent(runningTask.mIntentAgent);


//        runningTask.mNotification.setContentTitle(runningTask.getTitle())
//                .setContentText(runningTask.getTitle());

            runningTask.mNotification.show();
        }
    }

    protected synchronized void registerWork(RunningTask runningTask)
    {
        synchronized (getTaskList()) {
            getTaskList().add(runningTask);
        }

        publishNotification(runningTask);
    }

    public void run(final RunningTask runningTask)
    {
        mExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                runningTask.setService(WorkerService.this);
                registerWork(runningTask);
                runningTask.onRun();
                unregisterWork(runningTask);
                runningTask.setService(null);
            }
        });
    }

    protected synchronized void unregisterWork(RunningTask runningTask)
    {
        runningTask.mNotification.cancel();

        synchronized (getTaskList()) {
            getTaskList().remove(runningTask);
        }
    }

    public interface OnAttachListener
    {
        void onAttachedToTask(RunningTask task);
    }

    public abstract static class RunningTask<T extends OnAttachListener> extends InterruptAwareJob implements Serializable
    {
        private Interrupter mInterrupter;
        private WorkerService mService;
        private String mStatusText;
        private String mTitle;
        private int mIconRes;
        private long mLastNotified = 0;
        private int mHash = 0;
        private DynamicNotification mNotification;
        private IntentAgent mIntentAgent;
        private T mAnchorListener;

        public RunningTask()
        {
        }

        protected abstract void onRun();

        public void onInterrupted()
        {

        }

        public Interrupter getInterrupter()
        {
            if (mInterrupter == null)
                mInterrupter = new Interrupter();

            return mInterrupter;
        }

        public RunningTask<T> setInterrupter(Interrupter interrupter)
        {
            mInterrupter = interrupter;
            return this;
        }

        public void detachAnchor()
        {
            mAnchorListener = null;
        }

        @Override
        public int hashCode()
        {
            if (mHash != 0)
                return mHash;

            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof RunningTask && mIntentAgent != null) {
                RunningTask other = (RunningTask) obj;
                return mHash != 0 && other.mHash != 0 && mHash == other.mHash;
            }

            return super.equals(obj);
        }


        public T getAnchorListener()
        {
            return mAnchorListener;
        }

        public RunningTask<T> setAnchorListener(T listener)
        {
            mAnchorListener = listener;
            listener.onAttachedToTask(this);

            return this;
        }


        public IntentAgent getContentIntent()
        {
            return mIntentAgent;
        }

        public RunningTask<T> setContentIntent(IntentAgent intent)
        {
            mIntentAgent = intent;
            return this;
        }

        protected WorkerService getService()
        {
            return mService;
        }

        private void setService(WorkerService service)
        {
            mService = service;
        }

        public int getIconRes()
        {
            return mIconRes;
        }

        public RunningTask<T> setIconRes(int iconRes)
        {
            mIconRes = iconRes;
            return this;
        }

        public String getTitle()
        {
            return mTitle;
        }

        public RunningTask<T> setTitle(String title)
        {
            mTitle = title;
            return this;
        }

        public String getStatusText()
        {
            return mStatusText;
        }

        public boolean publishStatusText(String text)
        {
            mStatusText = text;

            if (System.currentTimeMillis() - mLastNotified > 2000) {
                mService.publishNotification(this);
                mLastNotified = System.currentTimeMillis();

                return true;
            }
            return false;
        }

        public RunningTask<T> setContentIntent(Context context, Intent intent)
        {
            mHash = intentHash(intent);
//            setContentIntent(IntentAgent.);
            return this;
        }

//        protected void run()
//        {
//            run(getInterrupter());
//        }
//
//        public boolean run(final Context context)
//        {
//            mService.run(this);
//            return true;
//        }
    }
}
