package com.example.eraofband.remote.sendimg

data class SendImgResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)