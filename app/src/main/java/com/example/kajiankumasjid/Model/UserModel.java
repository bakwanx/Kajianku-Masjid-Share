package com.example.kajiankumasjid.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id_mosque")
    String id_mosque;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("name_mosque")
    String name_mosque;
    @SerializedName("phone_number")
    String phone_number;
    @SerializedName("address")
    String address;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("img_profile")
    String img_profile;

    public String getId_mosque() {
        return id_mosque;
    }

    public void setId_mosque(String id_mosque) {
        this.id_mosque = id_mosque;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName_mosque() {
        return name_mosque;
    }

    public void setName_mosque(String name_mosque) {
        this.name_mosque = name_mosque;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImg_profile() {
        return img_profile;
    }

    public void setImg_profile(String img_profile) {
        this.img_profile = img_profile;
    }
}
