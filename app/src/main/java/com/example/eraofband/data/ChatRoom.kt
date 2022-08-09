package com.example.eraofband.data

data class ChatRoom(
    var chatRoomIdx: Int,
    var nickname: String,
    var profileImgUrl: String,
    var lastMessage: String,
    var updateAt: String,
    var isChecked: Boolean
)

data class MakeChatRoom(
    var chatRoomIdx : Int,
    var firstUserIdx : Int,
    var secondUserIdx : Int
)
