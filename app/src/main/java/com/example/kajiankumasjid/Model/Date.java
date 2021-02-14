package com.example.kajiankumasjid.Model;

import com.google.gson.annotations.SerializedName;

public class Date {
    @SerializedName("timestamp")
    private Integer timestamp;
    @SerializedName("gregorian")
    private String gregorian;
    @SerializedName("hijri")
    private String hijri;

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getGregorian() {
        return gregorian;
    }

    public void setGregorian(String gregorian) {
        this.gregorian = gregorian;
    }

    public String getHijri() {
        return hijri;
    }

    public void setHijri(String hijri) {
        this.hijri = hijri;
    }
}
