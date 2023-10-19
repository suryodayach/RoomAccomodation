package com.suryodayach.core.data

import com.google.gson.annotations.SerializedName

data class RoomsList(
    @SerializedName("rooms")
    val rooms: List<Room>
)