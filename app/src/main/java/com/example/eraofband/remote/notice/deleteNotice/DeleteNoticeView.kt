package com.example.eraofband.remote.notice.deleteNotice

interface DeleteNoticeView {
    fun onDeleteSuccess(result: String)
    fun onDeleteFailure(code: Int, message : String)
}