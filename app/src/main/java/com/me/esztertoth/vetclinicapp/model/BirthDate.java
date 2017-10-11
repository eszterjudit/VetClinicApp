package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.SerializedName;

public class BirthDate {
    @SerializedName("year")
    private int year;
    @SerializedName("monthValue")
    private int month;
    @SerializedName("dayOfMonth")
    private int dayOfMonth;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
