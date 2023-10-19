package com.suryodayach.core.authentication.accountmanager

import android.accounts.AccountManager
import android.os.Bundle
import javax.inject.Inject

class AuthToken @Inject constructor(
    private val accountManager: AccountManager
) {

    val options = Bundle()



}