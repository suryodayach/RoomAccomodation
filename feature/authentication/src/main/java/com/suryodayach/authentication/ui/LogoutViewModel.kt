package com.suryodayach.authentication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suryodayach.common.Utils.extractErrorMessage
import com.suryodayach.common.di.MainDispatcher
import com.suryodayach.core.authentication.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<LogoutUiState>(LogoutUiState.Initial)
    val uiState: StateFlow<LogoutUiState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _uiState.value = LogoutUiState.LogoutError(exception.message)
        Log.e("Tag", "Exception: ", exception)
    }

    fun logoutUser() {
            _uiState.value = LogoutUiState.LoggingOut
            viewModelScope.launch(errorHandler + dispatcher) {
                loginRepository.signOut().collect {
                    _uiState.value = when (it) {
                        is Exception -> LogoutUiState.LogoutError(it.extractErrorMessage())
                        else -> LogoutUiState.LoggedOut
                    }
                }
            }
        }
    }

sealed interface LogoutUiState {
    object Initial : LogoutUiState
    object LoggingOut : LogoutUiState
    object LoggedOut : LogoutUiState
    data class LogoutError(val error: String?) : LogoutUiState
}
