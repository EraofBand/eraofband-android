package com.example.eraofband.remote.chat.makeChat

data class MakeChatResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)