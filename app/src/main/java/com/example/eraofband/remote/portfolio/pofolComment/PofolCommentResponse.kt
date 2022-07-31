package com.example.eraofband.remote.portfolio.pofolComment

data class PofolCommentResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<PofolCommentResult>
)

data class PofolCommentResult(
    var content: String,
    var nickName: String,
    var pofolCommentIdx: Int,
    var pofolIdx: Int,
    var profileImgUrl: String,
    var updatedAt: String,
    var userIdx: Int
)