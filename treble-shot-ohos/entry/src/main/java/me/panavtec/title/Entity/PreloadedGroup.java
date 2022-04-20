package me.panavtec.title.Entity;

import com.ohos.trebleshot.object.TransferObject;

public class PreloadedGroup extends BaseSelectEntity{
    public int viewType;
    public String representativeText;

    public String assignees;

    public int totalCount;
    public int totalCountCompleted;
    public long totalBytes;
    public long totalBytesCompleted;
    public double totalPercent;
    public boolean isRunning;


    public long dateCreated;
    public String savePath;
    public boolean isServedOnWeb;

    //--------------------------
    public TransferObject.Flag flag = TransferObject.Flag.PENDING;
    public TransferObject.Type type = TransferObject.Type.OUTGOING;

    @Override
    public String getFilterStr() {
        return title;
    }



    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getRepresentativeText() {
        return representativeText;
    }

    public void setRepresentativeText(String representativeText) {
        this.representativeText = representativeText;
    }

    public String getAssignees() {
        return assignees;
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCountCompleted() {
        return totalCountCompleted;
    }

    public void setTotalCountCompleted(int totalCountCompleted) {
        this.totalCountCompleted = totalCountCompleted;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public long getTotalBytesCompleted() {
        return totalBytesCompleted;
    }

    public void setTotalBytesCompleted(long totalBytesCompleted) {
        this.totalBytesCompleted = totalBytesCompleted;
    }

    public double getTotalPercent() {
        return totalPercent;
    }

    public void setTotalPercent(double totalPercent) {
        this.totalPercent = totalPercent;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }


    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public boolean isServedOnWeb() {
        return isServedOnWeb;
    }

    public void setServedOnWeb(boolean servedOnWeb) {
        isServedOnWeb = servedOnWeb;
    }


}

