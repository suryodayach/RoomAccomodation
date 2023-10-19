package com.suryodayach.core.authentication.tokenmanager

import android.accounts.Account

interface TokenManager {
    fun getAuthToken(account: Account, authTokenType: String): String?
    fun setAuthToken(account: Account, authTokenType: String, token: String)
}