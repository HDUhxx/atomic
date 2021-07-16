package com.example.bassiness_card.datamodel;

import com.example.bassiness_card.typefactory.TypeFactory;

import ohos.agp.components.element.Element;

/**
 * DefaultDoubleLineList Item model
 */
public class DefaultDoubleLineListItemInfo implements ItemInfo {
    private final String firstLineText;
    private final String secondLineText;
    private final Element primaryImage;
    private final Element secondaryImage;

    /**
     * ListContent Constructor
     *
     * @param firstLineText    First line text
     * @param secondLineText   second line text
     * @param primaryImageId   primary image ID
     * @param secondaryImageId secondary image ID
     */
    public DefaultDoubleLineListItemInfo(String firstLineText,
                                         String secondLineText, Element primaryImageId, Element secondaryImageId) {
        this.firstLineText = firstLineText;
        this.secondLineText = secondLineText;
        this.primaryImage = primaryImageId;
        this.secondaryImage = secondaryImageId;
    }

    public String getFirstLineText() {
        return firstLineText;
    }

    public String getSecondLineText() {
        return secondLineText;
    }

    public Element getPrimaryImage() {
        return primaryImage;
    }

    public Element getSecondaryImage() {
        return secondaryImage;
    }

    @Override
    public int getType(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
