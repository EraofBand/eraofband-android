package com.example.eraofband.data

data class ChatRoom(
    var chatRoomIdx : Int = 0,
    var firstUserIdx : Int = 0,
    var secondUserIdx : Int = 0,
    var recentTime : Int = 0,
    var lastMessage : String = ""
)

data class MakeChatRoom(
    var chatRoomIdx : Int,
    var firstUserIdx : Int,
    var secondUserIdx : Int

)
