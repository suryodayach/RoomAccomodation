package com.suryodayach.core.data

import com.google.gson.annotations.SerializedName

data class ReduceAvailability(
    @SerializedName("reduce_amount")
    val reduceAmount: Int
)
