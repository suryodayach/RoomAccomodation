package com.suryodayach.core.data.di

import com.suryodayach.core.data.RoomsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomsModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://2w8xw6wlq5.execute-api.ap-southeast-2.amazonaws.com/")
            .build()
    }

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): RoomsApiService {
        return retrofit.create(RoomsApiService::class.java)
    }

}
