package com.liker.android.Group.model;

public class Person implements ListItem {

    String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_ITEM;
    }
}
