package com.example.eraofband.remote.chat.isChatRoom

interface IsChatRoomView {
    fun onGetSuccess(result: IsChatRoomResult)
    fun onGetFailure(code: Int, message: String)
}