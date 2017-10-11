package com.me.esztertoth.vetclinicapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Time implements Serializable {
    @SerializedName("hour")
    private int hour;
    @SerializedName("minute")
    private int minute;
    @SerializedName("second")
    private int second;
    @SerializedName("nano")
    private int nano;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getNano() {
        return nano;
    }

    public void setNano(int nano) {
        this.nano = nano;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
}
