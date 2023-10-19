package com.suryodayach.authentication.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suryodayach.common.Utils.extractErrorMessage
import com.suryodayach.common.di.MainDispatcher
import com.suryodayach.core.authentication.LoginRepository
import com.suryodayach.core.authentication.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Initial)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    var isEmailValid by mutableStateOf(true)
        private set

    var isPasswordValid by mutableStateOf(true)
        private set

    var isConfirmPasswordValid by mutableStateOf(true)
        private set

    var emailErrorMsg by mutableStateOf("")
        private set

    var passwordErrorMsg by mutableStateOf("")
        private set

    var confirmPasswordErrorMsg by mutableStateOf("")
        private set

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _uiState.value = SignUpUiState.SignUpError(exception.message)
    }

    fun updateEmail(input: String) {
        email = input
        validateEmail(input)
    }

    fun updatePassword(input: String) {
        password = input
        validatePassword(input)
        if (!confirmPassword.isEmpty()) {
            validateConfirmPassword(confirmPassword)
        }
    }

    fun updateConfirmPassword(input: String) {
        confirmPassword = input
        validateConfirmPassword(input)
    }

    fun validateEmail(input: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            isEmailValid = false
            emailErrorMsg = "Invalid email address"
        } else {
            isEmailValid = true
            emailErrorMsg = ""
        }
    }

    fun validatePassword(input: String) {
        if (input.length < 8) {
            isPasswordValid = false
            passwordErrorMsg = "Must be 8 characters"
        } else {
            isPasswordValid = true
            passwordErrorMsg = ""
        }
    }

    fun validateConfirmPassword(input: String) {
        if (input == password) {
            isConfirmPasswordValid = true
            confirmPasswordErrorMsg = ""
        } else {
            isConfirmPasswordValid = false
            confirmPasswordErrorMsg = "Password doesn't match"
        }
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun toggleShowConfirmPassword() {
        showConfirmPassword = !showConfirmPassword
    }

    fun registerUser() {
        if (isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            val user = User(
                email.trim(), password.trim()
            )
            _uiState.value = SignUpUiState.SigningUp
            viewModelScope.launch(errorHandler + dispatcher) {
                loginRepository.registerUser(user).collect {
                    _uiState.value = when (it) {
                        is Exception -> SignUpUiState.SignUpError(it.extractErrorMessage())
                        else -> SignUpUiState.OtpSent
                    }
                }
            }
        }
    }

}

sealed interface SignUpUiState {
    object Initial : SignUpUiState
    object SigningUp : SignUpUiState
    data class SignUpError(val error: String?) : SignUpUiState
    object OtpSent : SignUpUiState
}