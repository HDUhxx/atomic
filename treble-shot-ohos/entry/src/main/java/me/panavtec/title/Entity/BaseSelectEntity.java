package me.panavtec.title.Entity;

import me.panavtec.title.hminterface.EntityActionI;
import ohos.utils.net.Uri;

public class BaseSelectEntity implements EntityActionI {
    long groupId;
    long requestId;

    boolean isSelected = false;
    Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

//    ------------------------------------------------
//    mediaId + "", displayName, title, mine_type, duration, uri.toString(), data, date_added, size, volume_name));
//   tagmusic--27**Last_Stop.mp3**Last Stop**audio/mpeg**292059**dataability:///media/external/images/media/27**/hw_product/media/Pre-loaded/Music/Last_Stop.mp3**1616138906**11827423**external_primary**

    int mediaId;
    String displayName;
    String title;
    String mineType;
    String duration;
    String data;
    String dateAdded;
    String size;
    String volumeName;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSize() {
        return size;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    @Override
    public String getFilterStr() {
        return title;
    }
}
