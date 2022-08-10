package com.example.eraofband.data

data class ChatComment(
    var message: String = "",
    var readUser: Boolean = false,
    var timeStamp: Long = 0,
    var userIdx: Int = 0
)
