package com.example.eraofband.remote.notice.getNotice

interface GetNoticeView {
    fun onGetSuccess(result : ArrayList<GetNoticeResult>)
    fun onGetFailure(code: Int, message : String)
}