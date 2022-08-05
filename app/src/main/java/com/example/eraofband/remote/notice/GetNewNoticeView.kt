package com.example.eraofband.remote.notice

interface GetNewNoticeView {
    fun onGetSuccess(result: GetNewNoticeResult)
    fun onGetFailure(code: Int, message : String)
}