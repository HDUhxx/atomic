package me.panavtec.title.Entity;

public class TextStream extends BaseSelectEntity{
    private String content;
    public long id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    @Override
//    public String getFilterStr() {
//        return content;
//    }
}
