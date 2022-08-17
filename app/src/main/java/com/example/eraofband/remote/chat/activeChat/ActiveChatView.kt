package com.example.eraofband.remote.chat.activeChat

interface ActiveChatView {
    fun onActiveSuccess(result: String)
    fun onActiveFailure(code: Int, message : String)
}