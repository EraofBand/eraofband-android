package com.example.eraofband.remote.chat.getChatList

interface GetChatListView {
    fun onGetListSuccess(result : ArrayList<GetChatListResult>)
    fun onGetListFailure(code: Int, message : String)
}