package com.mvvmproject.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.mvvmproject.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiClient {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun create(): ApiInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun contextProvider(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}
