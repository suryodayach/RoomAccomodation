package com.suryodayach.core.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoomsApiService {

    @GET("room/{room_id}")
    suspend fun getRoom(@Path("room_id") roomId: String): Room

    @GET("rooms")
    suspend fun getAllRooms(): RoomsList

    @POST("room/{room_id}/add_rating")
    suspend fun addRating(@Path("room_id") roomId: String, @Body rating: Rating): Result

    @PUT("reduce_availability/{room_id}")
    suspend fun reduceAvailability(@Path("room_id") roomId: String, @Body reduceAvailability: ReduceAvailability): Result
}