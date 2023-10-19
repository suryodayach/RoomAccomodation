package com.suryodayach.core.authentication.di

import android.accounts.AccountManager
import android.content.Context
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions
import com.suryodayach.core.authentication.tokenmanager.AccountManagerTokenManager
import com.suryodayach.core.authentication.tokenmanager.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesCognitoUserPool(
        @ApplicationContext context: Context
    ): CognitoUserPool =
        CognitoUserPool(
            context,
            "ap-southeast-2_wluuZ0anM",
            "3c4t55vrjtbkciakdr6kovv09s",
            "1g98o4ji9qngibrc6lc8q6ki5h4tjv7dl4i3e5qpu3tf4vigi09f",
            ClientConfiguration(),
            Regions.AP_SOUTHEAST_2
        )

    @Provides
    @Singleton
    fun providesCredentialsProvider(
        @ApplicationContext context: Context
    ): CognitoCachingCredentialsProvider = CognitoCachingCredentialsProvider(
        context,
        "",
        Regions.AP_SOUTHEAST_2
    )

    @Provides
    @Singleton
    fun providesAccountManager(
        @ApplicationContext context: Context
    ): AccountManager {
        return AccountManager.get(context)
    }

    @Provides
    @Singleton
    fun providesTokenManager(accountManager: AccountManager): TokenManager {
        return AccountManagerTokenManager(accountManager)
    }
}