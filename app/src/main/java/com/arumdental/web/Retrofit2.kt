package com.arumdental.web

import com.arumdental.utils.Constants
import com.arumdental.utils.UserSharedPrefences
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class Retrofit2 {


    init    {

        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(200, TimeUnit.MINUTES)
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())

        httpClient.addInterceptor(object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

                var request: Request

                var token= UserSharedPrefences.getInstance().token
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


                        .build()
                }









                return chain.proceed(request)
            }
        })

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    }



    fun getapi(): ApiInterface {
        return retrofit?.create(ApiInterface::class.java)!!

    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val modified = response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()

            return modified
        }
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