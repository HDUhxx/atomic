package me.panavtec.bean;

public class PageData {

    public String filePath;
    public String type;//文件类型
    public String Num;
    public boolean isCanRead;
    public boolean isCanWrite;
    public Boolean isFloder;
    public String fileName;
    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public boolean isCanRead() {
        return isCanRead;
    }

    public void setCanRead(boolean canRead) {
        isCanRead = canRead;
    }

    public boolean isCanWrite() {
        return isCanWrite;
    }

    public void setCanWrite(boolean canWrite) {
        isCanWrite = canWrite;
    }

    public Boolean getFloder() {
        return isFloder;
    }

    public void setFloder(Boolean floder) {
        isFloder = floder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
