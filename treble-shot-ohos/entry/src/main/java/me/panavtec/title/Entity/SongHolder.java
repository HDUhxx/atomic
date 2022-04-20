package me.panavtec.title.Entity;

import me.panavtec.title.hmutils.TextUtils;
import ohos.utils.net.Uri;
@Deprecated
public class SongHolder extends Shareable {
    public String artist;
    public final String song;
    public String folder;
    public int albumId;
    public AlbumHolder albumHolder;


    @Override
    public String getFilterStr() {
        return song;
    }


    public SongHolder(int id, String song) {
        this.id = id;
        this.song = song;
    }

    public SongHolder(long id, String displayName, String artist, String song, String folder
            , String mimeType, int albumId, AlbumHolder albumHolder, long date, long size, Uri uri) {
        super(id, song + " - " + artist, displayName, mimeType, date, size, uri);

        this.artist = artist;
        this.song = song;
        this.folder = folder;
        this.albumId = albumId;
        this.albumHolder = albumHolder == null ? new AlbumHolder(albumId, "-", null) : albumHolder;
    }


    @Override
    public boolean applyFilter(String[] filteringKeywords) {
        if (super.applyFilter(filteringKeywords))
            return true;

        for (String keyword : filteringKeywords)
            if (folder.toLowerCase().contains(keyword.toLowerCase()))
                return true;

        return false;
    }

    @Override
    public String getComparableName() {
        return song;
    }

    @Override
    public boolean searchMatches(String searchWord) {

        return TextUtils.searchWord(artist, searchWord)
                || TextUtils.searchWord(song, searchWord)
                || TextUtils.searchWord(albumHolder.title, searchWord);
    }

}
