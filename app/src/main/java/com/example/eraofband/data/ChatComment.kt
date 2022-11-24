package com.example.eraofband.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatComment(
    var message: String = "",
    var readUser: Boolean = false,
    var timeStamp: Long = 0,
    var userIdx: Int = 0,
    @SerializedName("type") @Expose var type : Int = 0
)