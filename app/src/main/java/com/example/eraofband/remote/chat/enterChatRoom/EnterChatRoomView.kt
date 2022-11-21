package com.example.eraofband.remote.chat.enterChatRoom

interface EnterChatRoomView {
    fun onEnterSuccess(result: EnterChatRoomResult)
    fun onEnterFailure(code: Int, message : String)
}