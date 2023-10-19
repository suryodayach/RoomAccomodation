package com.suryodayach.core.data

import com.google.gson.annotations.SerializedName

data class Room(

    @SerializedName("roomId")
    val roomId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("room_type")
    val roomType: String,

    @SerializedName("availability")
    val availability: Int,

    @SerializedName("facilities")
    val facilities: List<String>,

    @SerializedName("location")
    val location: String,

    @SerializedName("photos")
    val photos: List<String>,

    @SerializedName("rating")
    val rating: Double
)