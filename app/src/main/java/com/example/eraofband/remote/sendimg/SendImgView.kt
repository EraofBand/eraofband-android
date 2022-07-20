package com.example.eraofband.remote.sendimg

interface SendImgView {
    fun onSendSuccess(response: SendImgResponse)
    fun onSendFailure(code: Int, message: String)
}