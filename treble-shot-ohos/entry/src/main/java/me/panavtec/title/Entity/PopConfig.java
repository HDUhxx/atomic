package me.panavtec.title.Entity;

import com.ohos.trebleshot.popupwindow.PopItemObject;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class PopConfig {
    public static final String MultipleChoose = "多选";
    public static final String NOTHING = "无";
    public static final String DATE = "日期";
    public static final String SIZE = "大小";
    public static final String SORTSHUN = "顺序排列";
    public static final String SORTDAO = "倒序排列";
    public static final String SORT_TITLE = "顺序倒序";


    public static final String GroupBy = "分组依据";
    public static final String SortBy = "排序方式";
    public static final String CreateFolder = "创建文件夹";
    public static final String MountFolder = "挂载文件夹";


    public static String[] items13 = {"多选", "分组依据", "排序方式"};

    public static String[] items2 = {"创建文件夹", "多选", "挂载文件夹", "排序方式"};

    private static final List<PopItemObject> endSort = new ArrayList<PopItemObject>() {
        {
            add(new PopItemObject("顺序排列", null));
            add(new PopItemObject("倒序排列", null));
        }
    };


    private static final List<PopItemObject> groupByList = new ArrayList<PopItemObject>() {
        {
            add(new PopItemObject("无", null));
            add(new PopItemObject("日期", null));
        }
    };

    private static final List<PopItemObject> sortByList = new ArrayList<PopItemObject>() {
        {
            add(new PopItemObject("日期", null));
            add(new PopItemObject("大小", null));
            add(new PopItemObject("顺序/倒序", endSort));
        }
    };


    private static final List<PopItemObject> sortByList2 = new ArrayList<PopItemObject>() {
        {
            add(new PopItemObject("文件名", null));
            add(new PopItemObject("日期", null));
            add(new PopItemObject("大小", null));
            add(new PopItemObject("顺序/倒序", endSort));
        }
    };

    private static final List<PopItemObject> sortByList3 = new ArrayList<PopItemObject>() {
        {
            add(new PopItemObject("文件名", null));
            add(new PopItemObject("日期", null));
            add(new PopItemObject("顺序/倒序", endSort));
        }
    };

    public static List<PopItemObject> createdItemObject1() {
        ArrayList<PopItemObject> popItemObjects = new ArrayList<>();
        PopItemObject multiple = new PopItemObject("多选", null);
        PopItemObject groupBys = new PopItemObject("分组依据", groupByList);
        PopItemObject SortBys = new PopItemObject("排序方式", sortByList);
        popItemObjects.add(multiple);
        popItemObjects.add(groupBys);
        popItemObjects.add(SortBys);
        return popItemObjects;
    }

    public static List<PopItemObject> createdItemObject2() {
        ArrayList<PopItemObject> popItemObjects = new ArrayList<>();
        PopItemObject SortBys = new PopItemObject("创建文件夹", null);
        PopItemObject SortBys1 = new PopItemObject("多选", null);
        PopItemObject SortBys2 = new PopItemObject("挂载文件夹", null);
        PopItemObject SortBys4 = new PopItemObject("排序方式", sortByList2);
        popItemObjects.add(SortBys);
        popItemObjects.add(SortBys1);
        popItemObjects.add(SortBys2);
        popItemObjects.add(SortBys4);
        return popItemObjects;
    }

    public static List<PopItemObject> createdItemObject3() {
        ArrayList<PopItemObject> popItemObjects = new ArrayList<>();
        PopItemObject multiple = new PopItemObject("多选", null);
        PopItemObject groupBys = new PopItemObject("分组依据", groupByList);
        PopItemObject SortBys = new PopItemObject("排序方式", sortByList3);
        popItemObjects.add(multiple);
        popItemObjects.add(groupBys);
        popItemObjects.add(SortBys);
        return popItemObjects;
    }

    public static List<String> getItemName(List<PopItemObject> list) {
        if (list == null || list.size() == 0) return new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (PopItemObject popItemObject : list) {
            String itemName = popItemObject.getItemName();
            names.add(itemName);
        }
        return names;
    }

    public static List<String> getItemNext(List<PopItemObject> list, String item) {
        if (list == null || list.size() == 0) return new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (PopItemObject popItemObject : list) {
            String itemName = popItemObject.getItemName();
            if (itemName!=null && itemName.equals(item)) {
                List<PopItemObject> nextProperty = popItemObject.getNextProperty();
                if (nextProperty != null && nextProperty.size() > 0) {
                    names = getItemName(nextProperty);
                    break;
                }
            } else {
                List<PopItemObject> nextProperty = popItemObject.getNextProperty();
                List<String> itemNext = getItemNext(nextProperty, item);
                if (itemNext != null && itemNext.size() > 0) {
                    names = itemNext;
                    break;
                }
            }
        }
        return names;
    }

    //根据路径key,获取value
    public static String getValue(Context context, String key) {
        HmSharedPerfrence instance = HmSharedPerfrence.getInstance(context.getApplicationContext());
        return instance.getParam(key);
    }

    //根据路径key,存储value
    public static void putValue(Context context, String value, String key) {
        HmSharedPerfrence instance = HmSharedPerfrence.getInstance(context);
        instance.saveParam(key, value);
    }

    //存储的值，是否与路劲相同
    private boolean isSelected(String dbStr, String content) {
        if (content==null||content.length()==0) return false;
        if (dbStr==null) return false;
        return dbStr.equals(content);
    }


    public static <T> String getKey(Class<T> tClass, String itemName) {
        return tClass.getSimpleName() + itemName;
    }
}
