package com.example.eraofband.remote.notice

interface GetNoticeView {
    fun onGetSuccess(result : List<GetNoticeResult>)
    fun onGetFailure(code: Int, message : String)
}