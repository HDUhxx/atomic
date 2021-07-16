package com.example.bassiness_card.datamodel;

import com.example.bassiness_card.typefactory.TypeFactory;

/**
 * Item model
 * Implement this interface to support other item model
 */
public interface ItemInfo {
    /**
     * Get layout Resource ID
     *
     * @param typeFactory typeFactory instance
     * @return Resource ID
     */
    int getType(TypeFactory typeFactory);
}
