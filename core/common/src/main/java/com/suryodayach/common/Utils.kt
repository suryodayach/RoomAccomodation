package com.suryodayach.common

object Utils {

    fun Exception.extractErrorMessage(): String? {
        return this.message?.split("(")?.firstOrNull()
    }

}