package com.suryodayach.core.authentication.tokenmanager

import android.accounts.Account
import android.accounts.AccountManager
import javax.inject.Inject

class AccountManagerTokenManager @Inject constructor(
    private val accountManager: AccountManager
): TokenManager {
    override fun getAuthToken(account: Account, authTokenType: String): String? {
        return accountManager.peekAuthToken(account, authTokenType)
    }

    override fun setAuthToken(account: Account, authTokenType: String, token: String) {
        accountManager.setAuthToken(account, authTokenType, token)
    }
}