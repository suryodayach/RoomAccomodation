package com.suryodayach.feature.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
class RoomDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roomRepository: RoomRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoomDetailsUiState>(RoomDetailsUiState.Loading)
    val uiState: StateFlow<RoomDetailsUiState> = _uiState.asStateFlow()

    var noOfRoom by mutableStateOf(1)
        private set

    var roomId = ""

    fun updateRooms(input: Int) {
        noOfRoom = input
    }

    private val errorHandler = CoroutineExceptionHandler {_, exception ->
        exception.printStackTrace()
        _uiState.value = RoomDetailsUiState.Error(exception.message)
    }

    init {
        roomId = savedStateHandle.get<String>("room_id").orEmpty()
        getRoomDetails(roomId)
    }

    private fun getRoomDetails(roomId: String) {
        viewModelScope.launch(errorHandler + dispatcher) {
            val room = roomRepository.getRoom(roomId)
            _uiState.value = RoomDetailsUiState.Success(room)
        }
    }

    fun bookRoom(roomId: String, noOfRooms: Int) {
        viewModelScope.launch(errorHandler + dispatcher) {
            val res = roomRepository.reduceAvailability(noOfRooms, roomId)
            if (res.message == "Availability reduced successfully") {
                _uiState.value = RoomDetailsUiState.Booked
            } else {
                _uiState.value = RoomDetailsUiState.ErrorBooking(res.message)
            }
        }
    }

}

sealed interface RoomDetailsUiState {
    object Loading: RoomDetailsUiState
    data class Success(val room: Room): RoomDetailsUiState
    data class Error(val error: String?): RoomDetailsUiState
    object Booked: RoomDetailsUiState
    data class ErrorBooking(val error: String?): RoomDetailsUiState
}