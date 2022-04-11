package com.lzh.nonview.router.activityresult;


import com.lzh.nonview.router.tools.Utils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ActivityResultDispatcher
 *
 * @since 2021-04-06
 */
public final class ActivityResultDispatcher {
    private static ActivityResultDispatcher dispatcher = new ActivityResultDispatcher();
    private Map<Ability, List<RequestArgs>> container = new HashMap<>();

    /**
     * 构造
     */
    private ActivityResultDispatcher() {
    }

    /**
     * get
     *
     * @return ActivityResultDispatcher
     */
    public static ActivityResultDispatcher get() {
        return dispatcher;
    }

    /**
     * bindRequestArgs
     *
     * @param activity
     * @param requestCode
     * @param callback
     */
    public void bindRequestArgs(Ability activity, int requestCode, ActivityResultCallback callback) {
        if (!Utils.isValid(activity)
                || callback == null
                || requestCode == -1) {
            return;
        }

        List<RequestArgs> list = findListByKey(activity);
        list.add(new RequestArgs(requestCode, callback));
    }

    /**
     * dispatchActivityResult
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     * @return boolean
     */
    public boolean dispatchActivityResult(Ability activity, int requestCode, int resultCode, Intent data) {
        if (!container.containsKey(activity)) {
            return false;
        }

        boolean handle = false;
        List<RequestArgs> list = findListByKey(activity);
        for (RequestArgs args : list) {
            if (args.requestCode == requestCode) {
                args.callback.onResult(resultCode, data);
                list.remove(args);
                handle = true;
                break;
            }
        }

        releaseInvalidItems();
        return handle;
    }

    // 移除无效的条目：比如当前activity在后台时被回收了
    private void releaseInvalidItems() {
        Iterator<Map.Entry<Ability, List<ActivityResultDispatcher.RequestArgs>>> iterator1 = container.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<Ability, List<ActivityResultDispatcher.RequestArgs>> next = iterator1.next();
            Ability nextAbility = next.getKey();
            if (!Utils.isValid(nextAbility)) {
                iterator1.remove();
            }
        }
    }

    /**
     * findListByKey
     *
     * @param activity
     * @return List<RequestArgs>
     */
    private List<RequestArgs> findListByKey(Ability activity) {
        List<RequestArgs> list = container.get(activity);
        if (list == null) {
            list = new ArrayList<>();
            container.put(activity, list);
        }
        return list;
    }

    /**
     * 主页面
     *
     * @since 2021-04-06
     */
    private static class RequestArgs {
        int requestCode;
        ActivityResultCallback callback;

        /**
         * RequestArgs
         *
         * @param requestCode
         * @param callback
         */
        public RequestArgs(int requestCode, ActivityResultCallback callback) {
            this.requestCode = requestCode;
            this.callback = callback;
        }
    }
}