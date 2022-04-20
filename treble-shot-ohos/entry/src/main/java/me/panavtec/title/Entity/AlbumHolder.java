package me.panavtec.title.Entity;

public class AlbumHolder {
    final int id;
    final String art;
    final String title;

    public AlbumHolder(int id, String title, String art) {
        this.id = id;
        this.art = art;
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AlbumHolder ? ((AlbumHolder) obj).id == id : super.equals(obj);
    }
}

