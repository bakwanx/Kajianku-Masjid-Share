package com.example.kajiankumasjid.ApiInterface;

import com.example.kajiankumasjid.Model.GeneralResponse;
import com.example.kajiankumasjid.Model.LoginModel;
import com.example.kajiankumasjid.Model.PostingModel;
import com.example.kajiankumasjid.Model.ResponsePrayerTime;
import com.example.kajiankumasjid.Model.SholatModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginModel> login_request(
            @Field("email") String email,
            @Field("password") String password
    );

    //get kajian
    @GET("posting/getKajian/{latitude}/{longitude}/{distance}")
    Call<ArrayList<PostingModel>> get_kajian_Default(
            @Path("latitude") String latitude,
            @Path("longitude") String longitude,
            @Path("distance") String jarak
    );

    //delete kajian
    @POST("posting/deletePosting/{id_post}")
    Call<GeneralResponse> deletePosting(
            @Path("id_post") String id_post
    );

    //update kajian
    @Multipart
    @POST("posting/Updatekajian/")
    Call<GeneralResponse> updateKajian(
            @Part("id_post") RequestBody id_post,
            @Part("id_mosque") RequestBody id_mosque,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("event_date") RequestBody event_date,
            @Part("event_time") RequestBody event_time,
            @Part("end_event_time") RequestBody end_event_time,
            @Part MultipartBody.Part photo
    );

    @FormUrlEncoded
    @POST("sholat/PostingSholat/")
    Call<GeneralResponse> postSholat(
        @Field("id_mosque") String id_mosque,
        @Field("adzan_subuh") String adzan_subuh,
        @Field("iqomah_subuh") String iqomah_subuh,
        @Field("adzan_dhuhur") String adzan_dhuhur,
        @Field("iqomah_dhuhur") String iqomah_dhuhur,
        @Field("adzan_ashar") String adzan_ashar,
        @Field("iqomah_ashar") String iqomah_ashar,
        @Field("adzan_maghrib") String adzan_maghrib,
        @Field("iqomah_maghrib") String iqomah_maghrib,
        @Field("adzan_isya") String adzan_isya,
        @Field("iqomah_isya") String iqomah_isya
    );

    //get kajian by id
    @GET("posting/getKajianbyId/{id_mosque}")
    Call<ArrayList<PostingModel>> getKajianById(
            @Path("id_mosque") String id_mosque
    );

    //posting kajian
    @Multipart
    @POST("posting/postkajian")
    Call<GeneralResponse> posting(
            @Part("id_mosque") RequestBody id_mosque,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("event_date") RequestBody event_date,
            @Part("event_time") RequestBody event_time,
            @Part("end_event_time") RequestBody end_event_time,
            @Part MultipartBody.Part photo
    );

    //get Time
    @GET("day.json")
    Call<ResponsePrayerTime> getTimeFromFatimah(
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("date") String date
    );
}
