package com.example.eraofband.remote.notice.getNewNotice

interface GetNewNoticeView {
    fun onGetSuccess(result: GetNewNoticeResult)
    fun onGetFailure(code: Int, message : String)
}