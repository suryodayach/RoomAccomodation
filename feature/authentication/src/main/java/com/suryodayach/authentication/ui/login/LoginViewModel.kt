package com.suryodayach.authentication.ui.login

import android.accounts.Account
import android.accounts.AccountManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.suryodayach.common.Utils.extractErrorMessage
import com.suryodayach.common.di.MainDispatcher
import com.suryodayach.core.authentication.LoginRepository
import com.suryodayach.core.authentication.tokenmanager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val tokenManager: TokenManager,
    private val accountManager: AccountManager,
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var isEmailValid by mutableStateOf(true)
        private set

    var isPasswordValid by mutableStateOf(true)
        private set

    var emailErrorMsg by mutableStateOf("")
        private set

    var passwordErrorMsg by mutableStateOf("")
        private set

    fun updateEmail(input: String) {
        email = input
        validateEmail(input)
    }

    fun updatePassword(input: String) {
        password = input
        validatePassword(input)
    }

    fun validateEmail(email: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isEmailValid = false
            emailErrorMsg = "Invalid email address"
        } else {
            isEmailValid = true
            emailErrorMsg = ""
        }
    }

    fun validatePassword(password: String) {
        if (password.length < 8) {
            isPasswordValid = false
            passwordErrorMsg = "Must be 8 characters"
        } else {
            isPasswordValid = true
            passwordErrorMsg = ""
        }
    }

    fun toggleShowPassword() {
//        val account: Account? = accountManager.getAccountsByType(LoginRepository.accountType).first()
//        val token = account?.let { tokenManager.getAuthToken(it, LoginRepository.authTokenType) }
//        Log.e(TAG, "toggleShowPassword: $token")
        showPassword = !showPassword
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _uiState.value = LoginUiState.LoginError(exception.message)
        Log.e(TAG, "Exception: ", exception)
    }

    fun loginUser() {
        validateEmail(email)
        validatePassword(password)
        if (isEmailValid && isPasswordValid) {
            _uiState.value = LoginUiState.LoggingIn
            viewModelScope.launch(errorHandler + dispatcher) {
                loginRepository.login(email.trim(), password.trim()).collect {
                    Log.d(TAG, "onLoginClicked: $it")
                    _uiState.value = when (it) {
                        is UserNotConfirmedException -> LoginUiState.UserNotConfirmed
                        is Exception -> LoginUiState.LoginError(it.extractErrorMessage())
                        else -> LoginUiState.LoginSuccess
                    }
                }
            }
        }
    }

    fun sendOtp() {
        viewModelScope.launch(errorHandler + dispatcher) {
            loginRepository.resendOtp(email.trim()).collect {
                when (it) {
                    is Exception -> _uiState.value =
                        LoginUiState.LoginError(it.extractErrorMessage())

                    else -> _uiState.value = LoginUiState.OtpSent
                }
            }
        }
    }
    companion object {
        private const val TAG = "LoginViewModel"
    }
}

sealed interface LoginUiState {
    object Initial : LoginUiState
    object LoggingIn : LoginUiState
    data class LoginError(val error: String?) : LoginUiState
    object LoginSuccess : LoginUiState
    object UserNotConfirmed : LoginUiState
    object OtpSent : LoginUiState
}
