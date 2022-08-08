package com.example.eraofband.remote.chat.patchChat

interface PatchChatView {
    fun onPatchSuccess(result: String)
    fun onPatchFailure(code: Int, message : String)
}