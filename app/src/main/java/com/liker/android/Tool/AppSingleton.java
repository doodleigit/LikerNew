package com.liker.android.Tool;

import com.liker.android.Home.model.PostItem;

public class AppSingleton {

    private String mimColor;
    private int hasMim;
    String groupId;



    private static final AppSingleton ourInstance = new AppSingleton();

    public static AppSingleton getInstance() {
        return ourInstance;
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
