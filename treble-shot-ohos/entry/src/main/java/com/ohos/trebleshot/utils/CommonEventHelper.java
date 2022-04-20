package com.ohos.trebleshot.utils;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.event.commonevent.*;

public class CommonEventHelper {
    public static void publish(String event,Intent intent){
        try {
            Operation operation = new Intent.OperationBuilder()
                    .withAction(event)
                    .build();
            intent.setOperation(operation);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void register(String event,OnCommonEventListener listener){
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(event); // 自定义事件
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        try {
            CommonEventManager.subscribeCommonEvent(new CommonEventSubscriber(subscribeInfo) {
                @Override
                public void onReceiveEvent(CommonEventData commonEventData) {
                    listener.onReceiveEvent(commonEventData);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void register(String event, SubCommonEventListener subCommonEventListener){
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(event); // 自定义事件
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        try {
            CommonEventSubscriber commonEventSubscriber = new CommonEventSubscriber(subscribeInfo) {
                @Override
                public void onReceiveEvent(CommonEventData commonEventData) {
                    subCommonEventListener.onReceiveEvent(commonEventData);
                }
            };
            subCommonEventListener.setOwner(commonEventSubscriber);
            CommonEventManager.subscribeCommonEvent(commonEventSubscriber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public interface  OnCommonEventListener{
        void onReceiveEvent(CommonEventData commonEventData);
    }

    public abstract static class SubCommonEventListener implements OnCommonEventListener{

        private CommonEventSubscriber mCommonEventSubscriber;

        public void setOwner(CommonEventSubscriber commonEventSubscriber){
            mCommonEventSubscriber = commonEventSubscriber;
        }

        public CommonEventSubscriber getOwner(){
            return mCommonEventSubscriber;
        }
    }
}
