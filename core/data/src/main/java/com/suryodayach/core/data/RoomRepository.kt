package com.suryodayach.core.data

import com.suryodayach.common.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
    private val restInterface: RoomsApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllRooms(): List<Room> {
        return withContext(dispatcher) {
            return@withContext restInterface.getAllRooms().rooms
        }
    }

    suspend fun getRoom(roomId: String): Room {
        return withContext(dispatcher) {
            return@withContext restInterface.getRoom(roomId)
        }
    }

    suspend fun reduceAvailability(amount: Int, roomId: String): Result {
        return withContext(dispatcher) {
            val reduceAvailability = ReduceAvailability(reduceAmount = amount)
            return@withContext restInterface.reduceAvailability(roomId, reduceAvailability)
        }
    }

}