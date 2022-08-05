package com.example.eraofband.remote.notice.getNewNotice

data class GetNewNoticeResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: GetNewNoticeResult
)

data class GetNewNoticeResult(
    var newAlarmExist: Int
)