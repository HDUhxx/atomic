package me.panavtec.title.Entity;


import me.panavtec.title.hminterface.Editable;
import me.panavtec.title.hmutils.TextUtils;
import ohos.utils.net.Uri;

/**
 * created by: Veli
 * date: 19.11.2017 16:50
 */

public abstract class Shareable extends BaseSelectEntity implements Editable {
    public long id;
    public String friendlyName;
    public String fileName;
    public String mimeType;
    public long date;
    public long size;


    public Shareable() {
    }

    public Shareable(long id, String friendlyName, String fileName, String mimeType, long date, long size, Uri uri) {
        this.id = id;
        this.friendlyName = friendlyName;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.date = date;
        this.size = size;
        this.uri = uri;
    }

    @Override
    public boolean applyFilter(String[] filteringKeywords) {
        for (String keyword : filteringKeywords)
            if (friendlyName != null && friendlyName.toLowerCase().contains(keyword.toLowerCase()))
                return true;

        return false;
    }

    @Override
    public boolean comparisonSupported() {
        return true;
    }

    @Override
    public boolean isSelectableSelected() {
        return isSelected;
    }

    @Override
    public String getComparableName() {
        return getSelectableTitle();
    }

    @Override
    public long getComparableDate() {
        return this.date;
    }

    @Override
    public long getComparableSize() {
        return this.size;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getSelectableTitle() {
        return this.friendlyName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Shareable ? ((Shareable) obj).uri.equals(uri) : super.equals(obj);
    }

    public boolean searchMatches(String searchWord) {
        return TextUtils.searchWord(this.friendlyName, searchWord);
    }

    @Override
    public boolean setSelectableSelected(boolean selected) {
        isSelected = selected;
        return true;
    }
}