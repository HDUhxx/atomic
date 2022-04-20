package me.panavtec.bean;

public class FristFolderData {
    public String showName;
    public int childCount;
    public String type;
    public boolean isCanRead;
    public String path;
    public boolean isCanWrite;
    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCanRead() {
        return isCanRead;
    }

    public void setCanRead(boolean canRead) {
        isCanRead = canRead;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCanWrite() {
        return isCanWrite;
    }

    public void setCanWrite(boolean canWrite) {
        isCanWrite = canWrite;
    }
}
