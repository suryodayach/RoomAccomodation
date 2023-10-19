package com.suryodayach.feature.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suryodayach.common.di.MainDispatcher
import com.suryodayach.core.data.Room
import com.suryodayach.core.data.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoomsUiState>(RoomsUiState.Loading)
    val uiState: StateFlow<RoomsUiState> = _uiState.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler {_, exception ->
        exception.printStackTrace()
        _uiState.value = RoomsUiState.Error(exception.message)
    }

    init {
        getAllRooms()
    }

    private fun getAllRooms() {
        viewModelScope.launch(errorHandler + dispatcher) {
            val rooms = roomRepository.getAllRooms()
            _uiState.value = RoomsUiState.Success(rooms)
        }
    }

}

sealed interface RoomsUiState {
    object Loading: RoomsUiState
    data class Success(val rooms: List<Room>): RoomsUiState
    data class Error(val error: String?): RoomsUiState
}