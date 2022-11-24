package com.example.eraofband.remote.sendimg

interface SendImgView {
    fun onSendSuccess(result: String)
    fun onSendFailure(code: Int, message: String)
}