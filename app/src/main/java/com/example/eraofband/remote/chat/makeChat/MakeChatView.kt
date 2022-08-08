package com.example.eraofband.remote.chat.makeChat

interface MakeChatView {
    fun onMakeSuccess(result: String)
    fun onMakeFailure(code: Int, message : String)
}