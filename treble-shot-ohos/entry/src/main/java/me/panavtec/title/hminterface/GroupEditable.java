package me.panavtec.title.hminterface;

public interface GroupEditable {
    int getViewType();

    int getRequestCode();

    String getRepresentativeText();

    void setRepresentativeText(CharSequence representativeText);

    boolean isGroupRepresentative();

    void setDate(long date);

    void setSize(long size);

}
