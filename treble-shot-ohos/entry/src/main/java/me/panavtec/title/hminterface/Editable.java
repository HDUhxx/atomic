package me.panavtec.title.hminterface;


/**
 * created by: Veli
 * date: 18.01.2018 20:57
 */

public interface Editable extends Comparable, Selectable {

    int VIEW_TYPE_REPRESENTATIVE = 100;
    int VIEW_TYPE_ACTION_BUTTON = 110;
    int MODE_GROUP_BY_NOTHING = 100;
    int MODE_GROUP_BY_DATE = 110;

    boolean applyFilter(String[] filteringKeywords);

    long getId();

    void setId(long id);
}
