package com.example.eraofband.remote.chat.enterChatRoom

data class EnterChatRoomResponse(
    var code: Int,
    var isSuccess: String,
    var message: String,
    var result: EnterChatRoomResult
)

data class EnterChatRoomResult (
    var lastChatIdx : Int
    )
