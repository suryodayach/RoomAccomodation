package com.suryodayach.core.authentication

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.os.Bundle
import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.suryodayach.core.authentication.model.User
import com.suryodayach.core.authentication.tokenmanager.TokenManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class LoginRepository @Inject constructor(
    private val cognitoUserPool: CognitoUserPool,
    private val tokenManager: TokenManager,
    private val accountManager: AccountManager,
//    private val credentialsProvider: CognitoCachingCredentialsProvider,
) {
    private var cognitoUser: CognitoUser? = null

    fun registerUser(user: User) = callbackFlow {
        val userAttributes = CognitoUserAttributes()
        userAttributes.addAttribute("email", user.email.trim())

        val signUpHandler = object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                cognitoUser = user
                trySend(signUpResult?.isUserConfirmed)
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUserPool.signUpInBackground(
            user.email.trim(),
            user.password.trim(),
            userAttributes,
            null,
            signUpHandler
        )

        awaitClose { }
    }


    fun verifyOtp(otp: String, email: String, password: String) = callbackFlow {
        val cognitoUser = cognitoUserPool.getUser(email.trim())

        val getDetailsHandler = object : GetDetailsHandler {
            override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
                Log.d(TAG, "onSuccess: ${cognitoUserDetails?.attributes?.attributes?.get("sub")}")
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                Log.d(TAG, "onSuccess: ${userSession?.idToken?.jwtToken}")

                cognitoUser.getDetailsInBackground(getDetailsHandler)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                val authDetail = AuthenticationDetails(
                    userId,
                    password.trim(),
                    null
                )
                authenticationContinuation?.setAuthenticationDetails(authDetail)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }
        }

        val genericHandler = object : GenericHandler {
            override fun onSuccess() {
                trySend("Otp Verified")
                cognitoUser.getSessionInBackground(authHandler)
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUser.confirmSignUpInBackground(otp.trim(), false, genericHandler)
        awaitClose { }
    }


    fun resendOtp(email: String) = callbackFlow {
        val cognitoUser = cognitoUserPool.getUser(email.trim())

        val verificationHandler = object : VerificationHandler {
            override fun onSuccess(verificationCodeDeliveryMedium: CognitoUserCodeDeliveryDetails?) {
                trySend(verificationCodeDeliveryMedium)
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUser.resendConfirmationCodeInBackground(verificationHandler)
        awaitClose { }
    }


    fun login(email: String, password: String) = callbackFlow {
        val cognitoUser = cognitoUserPool.getUser(email)

        val getDetailsHandler = object : GetDetailsHandler {
            override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
                Log.d(TAG, "onSuccess: ${cognitoUserDetails?.attributes?.attributes?.get("sub")}")
                trySend(cognitoUserDetails)
            }

            override fun onFailure(exception: Exception?) {
                Log.e(TAG, "onFailure: from details handler", exception)
                trySend(exception)
            }
        }

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                Log.d(TAG, "onSuccess: ${userSession?.idToken?.jwtToken}")
//                setAuthToken(email, userSession?.idToken?.jwtToken?:"")
                cognitoUser.getDetailsInBackground(getDetailsHandler)
                trySend(userSession)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                val authDetail = AuthenticationDetails(
                    userId,
                    password.trim(),
                    null
                )
                authenticationContinuation?.setAuthenticationDetails(authDetail)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUser.getSessionInBackground(authHandler)
        awaitClose { }
    }


    fun forgetPassword(email: String) = callbackFlow {
        val cognitoUser = cognitoUserPool.getUser(email)

        val forgotHandler = object : ForgotPasswordHandler {
            override fun onSuccess() {}

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                trySend("Otp Sent")
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUser.forgotPasswordInBackground(forgotHandler)
        awaitClose { }
    }


    fun resetPassword(email: String, oldPassword: String, newPassword: String) = callbackFlow {
        val cognitoUser = cognitoUserPool.getUser(email)

        val genericHandler = object : GenericHandler {
            override fun onSuccess() {
                trySend("Password changed successfully")
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }

        cognitoUser.changePasswordInBackground(
            oldPassword,
            newPassword,
            genericHandler
        )
        awaitClose { }
    }


    fun checkUserLoggedIn() = callbackFlow {
        val currentUser = cognitoUserPool.currentUser

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
//                createAccount()

//                Log.d(TAG, "onSuccess: jwt: ${userSession?.idToken?.jwtToken}")

//                setAuthToken(userSession?.idToken?.jwtToken?:"")

//                Log.e(TAG, "onSuccess: jwt" + getAuthToken("dark.soulst44@gmail.com"))

//                val jwtToken = userSession?.idToken?.jwtToken ?: ""
//                val account: Account? = accountManager.getAccountsByType(accountType).firstOrNull()
//                account?.let { tokenManager.setAuthToken(it, authTokenType, jwtToken) }

//                val logins: MutableMap<String, String> = HashMap()
//                logins["cognito-idp.ap-south-1.amazonaws.com/ap-south-1_IHQYsp93U"] = idToken ?: ""
//                credentialsProvider.logins = logins

                trySend(userSession)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }
        currentUser.getSessionInBackground(authHandler)
        awaitClose { }
    }


    fun signOut() = callbackFlow {
        val currentUser = cognitoUserPool.currentUser
        val genericHandler = object : GenericHandler {
            override fun onSuccess() {
                trySend("Signed Out")
            }

            override fun onFailure(exception: Exception?) {
                trySend(exception)
            }
        }
        currentUser.globalSignOutInBackground(genericHandler)
        awaitClose { }
    }

//    fun createAccount() {
//        val account = Account(accountName, accountType)
//        val accountAdded = accountManager.addAccountExplicitly(account, null, null)
//        Log.e(TAG, "createAccount: $accountAdded" )
//    }

    fun setAuthToken(accountName: String, jwtToken: String) {
        val accounts = accountManager.accounts
        var myAccount: Account? = null
        for (account in accounts) {
            if (account.name.equals(accountName)) {
                myAccount = account
                break
            }
        }
        if (myAccount == null) {
            myAccount = Account(accountName, accountType)
            accountManager.addAccountExplicitly(myAccount, null, null)
        }
        accountManager.setAuthToken(myAccount, authTokenType, jwtToken)
    }

    fun getAuthToken(accountName: String): String {
        val accounts = accountManager.accounts
        Log.e(TAG, "getAuthToken: " + accounts.size )
        val account = accounts.find { it.name == accountName }
        val authToken = accountManager.getAuthToken(account, authTokenType, null, false, null, null)
        return authToken.result.getString(authTokenType, "")
    }

    companion object {
        private const val TAG = "LoginRepository"
        const val accountType = "com.suryodayach.EventPlanner"
        const val authTokenType = "jwt_token"
    }

}
