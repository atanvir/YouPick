package com.youpic.apiConnection

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroClient
{
    fun getRetroClient(baseUrl:String)=Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(getRetroClientBuilder().build())
            .baseUrl(baseUrl)
            .build()

    fun getRetroClientBuilder():OkHttpClient.Builder{

        val okHttpClientBuilder=OkHttpClient.Builder();
        val loggingInterceptor=HttpLoggingInterceptor();
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.connectTimeout(120,TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(120,TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(120,TimeUnit.SECONDS)

        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        return okHttpClientBuilder
    }

    fun getApiClient(baseUrl:String):ApiInterface{
        return getRetroClient(baseUrl).create(ApiInterface::class.java);
    }

}