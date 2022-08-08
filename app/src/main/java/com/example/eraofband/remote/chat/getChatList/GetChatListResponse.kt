package com.example.eraofband.remote.chat.getChatList

data class GetChatListResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ArrayList<GetChatListResult>
)

data class GetChatListResult (
    var chatRoomIdx: Int,
    var nickName: String,
    var profileImgUrl: String
)