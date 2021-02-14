package com.example.kajiankumasjid.Model;

import com.google.gson.annotations.SerializedName;

public class SholatModel {
    @SerializedName("id_mosque")
    String id_mosque;
    @SerializedName("sholat")
    String sholat;
    @SerializedName("adzan")
    String adzan;
    @SerializedName("iqomah")
    String iqomah;

    public String getId_mosque() {
        return id_mosque;
    }

    public void setId_mosque(String id_mosque) {
        this.id_mosque = id_mosque;
    }

    public String getSholat() {
        return sholat;
    }

    public void setSholat(String sholat) {
        this.sholat = sholat;
    }

    public String getAdzan() {
        return adzan;
    }

    public void setAdzan(String adzan) {
        this.adzan = adzan;
    }

    public String getIqomah() {
        return iqomah;
    }

    public void setIqomah(String iqomah) {
        this.iqomah = iqomah;
    }
}
