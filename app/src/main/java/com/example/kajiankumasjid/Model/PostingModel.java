package com.example.kajiankumasjid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostingModel implements Parcelable {
    @SerializedName("id_post")
    String id_post;
    @SerializedName("id_mosque")
    String id_mosque;
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("event_date")
    String event_date;
    @SerializedName("event_time")
    String event_time;
    @SerializedName("end_event_time")
    String end_event_time;
    @SerializedName("image")
    String image;
    @SerializedName("post_date")
    String pos_date;
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


    protected PostingModel(Parcel in) {
        id_post = in.readString();
        id_mosque = in.readString();
        title = in.readString();
        description = in.readString();
        event_date = in.readString();
        event_time = in.readString();
        end_event_time = in.readString();
        image = in.readString();
        pos_date = in.readString();
        name_mosque = in.readString();
        phone_number = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        img_profile = in.readString();
    }


    public static final Creator<PostingModel> CREATOR = new Creator<PostingModel>() {
        @Override
        public PostingModel createFromParcel(Parcel in) {
            return new PostingModel(in);
        }

        @Override
        public PostingModel[] newArray(int size) {
            return new PostingModel[size];
        }
    };

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getId_mosque() {
        return id_mosque;
    }

    public void setId_mosque(String id_mosque) {
        this.id_mosque = id_mosque;
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

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String even_time) {
        this.event_time = even_time;
    }

    public String getEnd_event_time() {
        return end_event_time;
    }

    public void setEnd_event_time(String end_event_time) {
        this.end_event_time = end_event_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPos_date() {
        return pos_date;
    }

    public void setPos_date(String pos_date) {
        this.pos_date = pos_date;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_post);
        parcel.writeString(id_mosque);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(event_date);
        parcel.writeString(event_time);
        parcel.writeString(end_event_time);
        parcel.writeString(image);
        parcel.writeString(pos_date);
        parcel.writeString(name_mosque);
        parcel.writeString(phone_number);
        parcel.writeString(address);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(img_profile);
    }
}
