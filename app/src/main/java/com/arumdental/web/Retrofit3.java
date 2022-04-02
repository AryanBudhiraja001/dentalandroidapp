package com.arumdental.web;

import android.util.Log;

import com.arumdental.utils.Constants;
import com.arumdental.utils.OAuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit3 {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    OAuthInterceptor oauth1Woocommerce = new OAuthInterceptor.Builder()
            .consumerKey(Constants.CONSUMER_KEY)
            .consumerSecret(Constants.CONSUMER_SECRET)
            .build();
    private Retrofit retrofit = null;

    public ApiInterface getAPI() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(oauth1Woocommerce)
                .addInterceptor(provideHttpLoggingInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);


        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("Injector", message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return httpLoggingInterceptor;
    };
}
