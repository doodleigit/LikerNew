package com.liker.android.Tool;

public class AppSingleton {

    private String mimColor;
    private int hasMim;

    private static final AppSingleton ourInstance = new AppSingleton();

    public static AppSingleton getInstance() {
        return ourInstance;
    }

    private AppSingleton() {
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
