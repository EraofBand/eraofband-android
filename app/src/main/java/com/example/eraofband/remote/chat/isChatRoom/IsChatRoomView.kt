package com.example.eraofband.remote.chat.isChatRoom

interface IsChatRoomView {
    fun onExistsSuccess(result: IsChatRoomResult)
    fun onExistsFailure(code: Int, message: String)
}