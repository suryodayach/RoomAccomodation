package com.suryodayach.authentication.ui.otpverification

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suryodayach.common.Utils.extractErrorMessage
import com.suryodayach.common.di.MainDispatcher
import com.suryodayach.core.authentication.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginRepository: LoginRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<OtpVerificationUiState>(OtpVerificationUiState.OtpSent)
    val uiState: StateFlow<OtpVerificationUiState> = _uiState.asStateFlow()

    private val _verifyOtpValue: MutableState<VerifyOtpValue> = mutableStateOf(VerifyOtpValue())
    val verifyOtpValue get() = _verifyOtpValue

    var otp by mutableStateOf("")
        private set

    fun updateOtp(input: String) {
        otp = input
    }

    private val timer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(time: Long) {
            val countDownTime = time / 1000
            _verifyOtpValue.value = _verifyOtpValue.value.copy(countdownTime = "00:$countDownTime")
        }

        override fun onFinish() {
            _verifyOtpValue.value = _verifyOtpValue.value.copy(countdownTime = null)
        }
    }

    init {
        val email = savedStateHandle.get<String>("email").orEmpty()
        val password = savedStateHandle.get<String>("password").orEmpty()
        _verifyOtpValue.value = _verifyOtpValue.value.copy(email = email, password = password)
        startCountDownTimer()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _uiState.value = OtpVerificationUiState.OtpVerificationError(exception.message)
    }

    private fun startCountDownTimer() {
        timer.start()
    }

    private fun stopCountDownTimer() {
        timer.cancel()
        _verifyOtpValue.value = _verifyOtpValue.value.copy(countdownTime = null)
    }

    fun onVerifyOtpClicked() {
        viewModelScope.launch(errorHandler + dispatcher) {
            _uiState.value = OtpVerificationUiState.OtpVerifying
            loginRepository.verifyOtp(
                otp = otp.trim(),
                email = _verifyOtpValue.value.email.trim(),
                password = _verifyOtpValue.value.password.trim()
            ).collect {
                _uiState.value = when (it) {
                    is Exception ->
                        OtpVerificationUiState.OtpVerificationError(it.extractErrorMessage())

                    else -> OtpVerificationUiState.OtpVerified
                }
            }
        }
    }

    private fun resendOtp(email: String) = loginRepository.resendOtp(email)

    fun onResendOtpClicked() {
        viewModelScope.launch(errorHandler + dispatcher) {
            startCountDownTimer()
            resendOtp(_verifyOtpValue.value.email.trim()).collect {
                _uiState.value = when (it) {
                    is Exception -> OtpVerificationUiState.ResendOtpError(it.extractErrorMessage())
                    else -> OtpVerificationUiState.OtpSent
                }
            }
        }
    }
}

data class VerifyOtpValue(
    val email: String = "",
    val password: String = "",
    val countdownTime: String? = null
)

sealed interface OtpVerificationUiState {
    object OtpSent : OtpVerificationUiState
    object OtpVerifying : OtpVerificationUiState
    object OtpVerified : OtpVerificationUiState
    data class OtpVerificationError(val error: String?) : OtpVerificationUiState
    data class ResendOtpError(val error: String?) : OtpVerificationUiState
}