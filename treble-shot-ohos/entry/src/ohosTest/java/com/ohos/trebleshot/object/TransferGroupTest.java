package com.ohos.trebleshot.object;


import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.utils.AppUtils;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransferGroupTest {
    private AccessDatabase database;
    private final String tableName = AccessDatabase.TABLE_TRANSFERGROUP;

    @Before
    public void setUp(){
        database = AppUtils.getDatabase(AbilityDelegatorRegistry.getAbilityDelegator().getAppContext());
    }
    @Test
    public void test_01_InsertOne() {
        TransferGroup object = new TransferGroup();
        object.groupId=System.currentTimeMillis();
        object.isServedOnWeb=true;
        object.savePath = "1";
        object.dateCreated = System.currentTimeMillis();
        database.publish(object);
    }

    private void testCount(int num){
        int count = database.getTable(new SQLQuery.Select(tableName)).size();
        Assert.assertEquals("查询数量:",count,num);
    }

    @Test
    public void test_02_CountNum(){
        test_01_InsertOne();
        testCount(2);
    }



    @Test
    public void test_04_Delete(){
       List<CursorItem> items =  database.getTable(new SQLQuery.Select(tableName));
       Assert.assertEquals("删除前源数据",items.size(),2);
       //删除方式1
       CursorItem item =  items.get(0);
       TransferGroup p = new TransferGroup();
       p.reconstruct(item);
       database.remove(p);
       testCount(1);
       //删除方式2
       database.remove(new SQLQuery.Select(tableName).setWhere("savePath=?","1"));
       testCount(0);
    }

    @Test
    public void test_03_modify(){
        List<CursorItem> items =  database.getTable(new SQLQuery.Select(tableName));
        Assert.assertEquals("源数据",items.size(),2);
        CursorItem item =  items.get(0);
        TransferGroup p = new TransferGroup();
        p.reconstruct(item);
        p.savePath = "2";
        database.publish(p);
        int num2= database.getTable(new SQLQuery.Select(tableName).setWhere("savePath=?","2")).size();
        Assert.assertEquals("修改后查询",num2,1);

    }



}