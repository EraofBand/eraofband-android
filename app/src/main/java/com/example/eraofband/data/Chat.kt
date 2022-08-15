package com.example.eraofband.data


data class Chat(
    var message: String = "",
    var readUser: Boolean = false,
    var timeStamp: Long = 0,
    var userIdx: Int = 0,
    val viewType: Int
)/*{
    companion object {
        const val VIEW_TYPE_LEFT = 0
        const val VIEW_TYPE_RIGHT = 1
    }
}*/