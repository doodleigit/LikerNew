package com.liker.android.Tool;


public class AppSingleton {

    private String mimColor;
    private int hasMim;
    private String groupId;
    private boolean isMember;
    private String pageAboutDescription;


    private static final AppSingleton ourInstance = new AppSingleton();

    public static AppSingleton getInstance() {
        return ourInstance;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public String getPageAboutDescription() {
        return pageAboutDescription;
    }

    public void setPageAboutDescription(String pageAboutDescription) {
        this.pageAboutDescription = pageAboutDescription;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMimColor() {
        return mimColor;
    }

    public void setMimColor(String mimColor) {
        this.mimColor = mimColor;
    }

    public int getHasMim() {
        return hasMim;
    }

    public void setHasMim(int hasMim) {
        this.hasMim = hasMim;
    }
}
