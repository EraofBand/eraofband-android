package com.example.eraofband.data


data class Chat(
    var chatIdx : Int,
    var comments: ArrayList<ChatContent>,
    var profileImgUrl: String,
    var nickName : String,
    var users : ArrayList<Users>,
    val viewType: Int
){
    companion object {
        const val VIEW_TYPE_LEFT = 0
        const val VIEW_TYPE_RIGHT = 1
    }
}

data class Users(
    var firstUserIdx: Int,
    var secondUserIdx : Int
)

data class ChatContent(
    var commentIdx : Int,
    var message : String,
    var readUser : ArrayList<ReadUser>,
    var timeStamp: String,
    var userIdx : Int
)

data class ReadUser(
    var userIdx : Int,
    var check : Boolean
)