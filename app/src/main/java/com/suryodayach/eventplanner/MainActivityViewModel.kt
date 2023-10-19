package com.suryodayach.eventplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MainActivityViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _uiState.value = MainActivityUiState.LoginError(exception.message)
    }

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        _uiState.value = MainActivityUiState.Loading
        viewModelScope.launch(errorHandler + dispatcher) {
            loginRepository.checkUserLoggedIn().collect {
                _uiState.value = when (it) {
                    is Exception -> MainActivityUiState.LoggedOut
                    else -> MainActivityUiState.LoggedIn
                }
            }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    object LoggedIn : MainActivityUiState
    data class LoginError(val error: String?) : MainActivityUiState
    object LoggedOut : MainActivityUiState
}