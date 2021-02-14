package com.example.kajiankumasjid.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("user_data")
    private UserModel userModel;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
