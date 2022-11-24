package com.example.eraofband.remote.chat.leaveChat

interface LeaveChatView {
    fun onLeaveSuccess(result: String)
    fun onLeaveFailure(code: Int, message : String)
}