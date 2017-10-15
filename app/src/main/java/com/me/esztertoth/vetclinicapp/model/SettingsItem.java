package com.me.esztertoth.vetclinicapp.model;

import android.graphics.drawable.Drawable;

public class SettingsItem {

    private Drawable icon;
    private String title;
    private String description;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
