package com.example.bassiness_card.viewholder;

import com.example.bassiness_card.ResourceTable;
import com.example.bassiness_card.datamodel.SingleButtonDoubleLineListItemInfo;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * SingleButtonDoubleLineList View holder
 * 列渲染
 */
public class SingleButtonDoubleLineList extends ViewHolder {
    private final Text textPrimary;
    private final Text textSecondary;
    private final Image image;

    /**
     * Constructor
     *
     * @param itemComponent itemComponent
     */
    public SingleButtonDoubleLineList(Component itemComponent) {
        textPrimary = (Text) itemComponent.findComponentById(ResourceTable.Id_singleButtonList_text_primary);
        textSecondary = (Text) itemComponent.findComponentById(ResourceTable.Id_singleButtonList_text_secondary);
        image = (Image) itemComponent.findComponentById(ResourceTable.Id_singleButtonList_right_img);
    }

    /**
     * Set up Double-line list item components
     *
     * @param model   item model
     * @param context context
     */
    @Override
    public <T> void setUpComponent(T model, Context context) {
        if (model instanceof SingleButtonDoubleLineListItemInfo) {
            setUpComponent((SingleButtonDoubleLineListItemInfo) model, context);
        }
    }

    private void setUpComponent(SingleButtonDoubleLineListItemInfo model, Context context) {
        textPrimary.setText(model.getFirstLineText());
        textSecondary.setText(model.getSecondLineText());
        image.setImageElement(model.getImage());
    }
}
