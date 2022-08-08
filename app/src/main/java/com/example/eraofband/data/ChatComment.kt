package com.example.eraofband.data

data class ChatComment(
    var message: String,
    var readUser: Boolean,
    var timeStamp: Int,
    var userIdx: Int
)
