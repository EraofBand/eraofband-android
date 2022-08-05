package com.example.eraofband.remote.notice.deleteNotice

data class DeleteNoticeResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)
