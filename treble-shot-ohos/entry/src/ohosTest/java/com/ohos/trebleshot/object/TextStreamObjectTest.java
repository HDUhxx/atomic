package com.ohos.trebleshot.object;


import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.CursorItem;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.utils.AppUtils;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.utils.net.Uri;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextStreamObjectTest {
    private AccessDatabase database;
    private final String tableName = AccessDatabase.TABLE_CLIPBOARD;

    @Before
    public void setUp() {
        database = AppUtils.getDatabase(AbilityDelegatorRegistry.getAbilityDelegator().getAppContext());
    }

    @Test
    public void test_01_InsertOne() {
        TextStreamObject object = new TextStreamObject();
        object.id = System.currentTimeMillis();
        object.text = "1";
        database.publish(object);
    }

    private void testCount(int num) {
        int count = database.getTable(new SQLQuery.Select(tableName)).size();
        Assert.assertEquals("查询数量:", count, num);
    }

    @Test
    public void test_02_CountNum() {
        test_01_InsertOne();
        testCount(2);
    }


    @Test
    public void test_04_Delete() {
        List<CursorItem> items = database.getTable(new SQLQuery.Select(tableName));
        Assert.assertEquals("删除前源数据", items.size(), 2);
        //删除方式1
        CursorItem item = items.get(0);
        TextStreamObject p = new TextStreamObject();
        p.reconstruct(item);
        database.remove(p);
        testCount(1);
        //删除方式2
        database.remove(new SQLQuery.Select(tableName).setWhere("text=?", "1"));
        testCount(0);
    }

    @Test
    public void test_03_modify() {
        List<CursorItem> items = database.getTable(new SQLQuery.Select(tableName));
        Assert.assertEquals("源数据", items.size(), 2);
        CursorItem item = items.get(0);
        TextStreamObject p = new TextStreamObject();
        p.reconstruct(item);
        p.text = "2";
        database.publish(p);
        int num2 = database.getTable(new SQLQuery.Select(tableName).setWhere("text=?", "2")).size();
        Assert.assertEquals("修改后查询", num2, 1);

    }


}