package com.example.eraofband.remote.notice

data class GetNoticeResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetNoticeResult>
)

data class GetNoticeResult (
    var noticeBody: String,
    var noticeHead: String,
    var noticeIdx: Int,
    var noticeImg: String,
    var status: String,
    var updatedAt: String
)