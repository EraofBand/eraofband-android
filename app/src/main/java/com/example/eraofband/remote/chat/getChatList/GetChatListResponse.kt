package com.example.eraofband.remote.chat.getChatList

data class GetChatListResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ArrayList<GetChatListResult>
)

data class GetChatListResult (
    var chatRoomIdx: String,
    var nickName: String,
    var otherUserIdx: Int,
    var profileImgUrl: String,
    var status : Int
)