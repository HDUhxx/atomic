package com.example.myapplication.custom.data;

import com.example.myapplication.custom.categorylist.HeadItem;
import com.example.myapplication.custom.categorylist.SearchItem;
import com.example.myapplication.custom.categorylist.SingleListItem;
import com.example.myapplication.custom.categorylist.CategoryItemFactory;
import com.example.myapplication.custom.categorylist.TestCategory;
import com.example.myapplication.datamodel.Category;
import com.example.myapplication.datamodel.CategoryItemBase;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Some custom data to display.
 */
public class CustomData {
    private static final int[][] CATEGORY_DATA = new int[][]{
            {SearchItem.ITEM_TYPE},
            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE},
            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE},
            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE},
//            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE,
//                    SingleListItem.ITEM_TYPE},
//            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE,
//                    SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE},
//            {HeadItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE,
//                    SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE, SingleListItem.ITEM_TYPE},
    };

    private static String[] CATEGORY_TEXT={"本人","家人","客户"} ;
//    private static String CATEGORY_TEXT="本人" ;

    private static final String[] SINGLE_LEFT_TEXT = {"张某某","爸爸","妈妈","张总","李总","赵总"};

    private static final String SINGLE_RIGHT_TEXT = "17500000000";

    /**
     * Return some custom data to display.
     *
     * @param context current context
     * @return custom category list
     */
    public static List<Category> getCustomData(Context context) {
        ArrayList<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < CATEGORY_DATA.length; i++) {
            int[] items = CATEGORY_DATA[i];
            ArrayList<CategoryItemBase> itemList = new ArrayList<>();
            for (int j = 0; j < items.length; j++) {
                switch (items[j]) {
                    case HeadItem.ITEM_TYPE:
                        itemList.add(CategoryItemFactory.createListItem(context, HeadItem.ITEM_TYPE,
                                CATEGORY_TEXT [i-1]));
                        break;
                    case SingleListItem.ITEM_TYPE:
                        itemList.add(CategoryItemFactory.createListItem(context, SingleListItem.ITEM_TYPE,
                                SINGLE_LEFT_TEXT[(i+j-2)], SINGLE_RIGHT_TEXT));
                        break;
                    case SearchItem.ITEM_TYPE:
                        itemList.add(CategoryItemFactory.createListItem(context, SearchItem.ITEM_TYPE));
                        break;
                    default:
                        break;
                }
            }
            TestCategory testCategory = new TestCategory(context, itemList, i);
            categoryList.add(testCategory);
        }
        return categoryList;
    }
}
