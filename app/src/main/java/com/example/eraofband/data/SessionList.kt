package com.example.eraofband.data

data class SessionList(
    var session: String,  // 세션
    var count: Int,  // 모집 인원
    var sessionComment: String  // 세션별로 남길 말
)
