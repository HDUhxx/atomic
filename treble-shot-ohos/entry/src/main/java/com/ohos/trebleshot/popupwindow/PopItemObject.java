package com.ohos.trebleshot.popupwindow;

import java.util.List;

public class PopItemObject {
    private String itemName;

    private List<PopItemObject> nextProperty;

    private boolean isSelected = false;

    public PopItemObject(String itemName, List<PopItemObject> nextProperty) {
        this.itemName = itemName;
        this.nextProperty = nextProperty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<PopItemObject> getNextProperty() {
        return nextProperty;
    }

    public void setNextProperty(List<PopItemObject> nextProperty) {
        this.nextProperty = nextProperty;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
