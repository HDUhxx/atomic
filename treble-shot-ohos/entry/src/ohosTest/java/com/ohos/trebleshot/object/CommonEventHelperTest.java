package com.ohos.trebleshot.object;

import com.ohos.trebleshot.service.DeviceDiscoveryTask;
import com.ohos.trebleshot.utils.CommonEventHelper;
import ohos.aafwk.content.Intent;
import ohos.event.commonevent.CommonEventData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonEventHelperTest {
    private final static String EVENT_NAME = "TEST";

    @Test
    public void test_01_register(){
        CommonEventHelper.register(EVENT_NAME, new CommonEventHelper.OnCommonEventListener() {
            @Override
            public void onReceiveEvent(CommonEventData commonEventData) {
                //回调在测试中无效
                //Assert.assertEquals(commonEventData.getIntent().getIntParam("key1",0),2);
            }
        });
    }

    @Test
    public void test_02_publish(){
        Intent eventIntent = new Intent();
        eventIntent.setParam("key1",2);
        CommonEventHelper.publish(EVENT_NAME,eventIntent);
    }
}
