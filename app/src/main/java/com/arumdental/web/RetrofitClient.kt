package com.arumdental.web

import android.util.Log
import com.arumdental.BuildConfig
import com.arumdental.utils.Constants
import com.arumdental.utils.UserSharedPrefences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitClient {


    init    {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(200, TimeUnit.MINUTES)
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(logging)


        httpClient.addInterceptor(object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                var request: Request

                var token=UserSharedPrefences.getInstance().token
                println("token "+token)
                if(!token.isNullOrEmpty())
                {
                    request =  chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Authorization", "Bearer $token").build()


                }
                else {
                    request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build()
                }









                return chain.proceed(request)
            }
        })



        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())


            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }



    fun getapi(): ApiInterface {
        return retrofit?.create(ApiInterface::class.java)!!

    }

    companion object{
        private  var retrofit : Retrofit?=null
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance():RetrofitClient{
            if(instance==null){
                instance= RetrofitClient()
            }
            return instance as RetrofitClient


        }
    }

}