package com.example.eraofband.remote.chat.isChatRoom

data class IsChatRoomResponse(
    var code: Int,
    var isSuccess: String,
    var message: String,
    var result: IsChatRoomResult
)

data class IsChatRoomResult(
    var chatRoomIdx: String?
)