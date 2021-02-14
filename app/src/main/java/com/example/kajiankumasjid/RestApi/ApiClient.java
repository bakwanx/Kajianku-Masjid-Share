package com.example.kajiankumasjid.RestApi;

import com.example.kajiankumasjid.Utilities.GlobalConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String base_url = GlobalConfig.base_url;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit==null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();

        }
        return retrofit;
    }
}
