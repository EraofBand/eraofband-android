package com.example.eraofband.data

data class ChatRoom(
    var chatRoomIdx: String,
    var nickname: String,
    var profileImgUrl: String,
    var lastMessage: String,
    var updateAt: String,
    var isChecked: Boolean
)

data class MakeChatRoom(
    var chatRoomIdx : String,
    var firstUserIdx : Int,
    var secondUserIdx : Int
)
