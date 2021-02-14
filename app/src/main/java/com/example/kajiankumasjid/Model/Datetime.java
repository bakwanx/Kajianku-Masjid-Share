package com.example.kajiankumasjid.Model;

import com.google.gson.annotations.SerializedName;

public class Datetime {
    @SerializedName("times")
    private Times times;

    @SerializedName("date")
    private Date date;

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
