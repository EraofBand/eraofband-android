package com.example.eraofband.remote.portfolio

data class PofolCommentWriteResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: PofolCommentWriteResult
)
data class PofolCommentWriteResult(
    var content: String,
    var nickName: String,
    var pofolCommentIdx: Int,
    var pofolIdx: Int,
    var profileImgUrl: String,
    var updatedAt: String,
    var userIdx: Int
)