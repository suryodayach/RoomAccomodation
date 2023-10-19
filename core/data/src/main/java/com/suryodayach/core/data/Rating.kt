package com.suryodayach.core.data

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("rating")
    val rating: Int
)
