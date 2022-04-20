package com.ohos.trebleshot.object;



/**
 * created by: Veli
 * date: 18.01.2018 20:57
 */

public interface Editable extends Comparable, Selectable
{
    boolean applyFilter(String[] filteringKeywords);

    long getId();

    void setId(long id);
}
