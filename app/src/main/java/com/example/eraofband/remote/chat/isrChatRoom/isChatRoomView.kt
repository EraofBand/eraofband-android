package com.example.eraofband.remote.chat.isrChatRoom

import com.example.eraofband.remote.chat.isChatRoom.IsChatRoomResult

interface IsChatRoomView {
    fun onExistsSuccess(result: IsChatRoomResult)
    fun onExistsFailure(code: Int, message: String)
}