package com.example.eraofband.remote.chat.patchChat

data class PatchChatResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)