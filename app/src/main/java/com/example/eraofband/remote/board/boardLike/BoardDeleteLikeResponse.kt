package com.example.eraofband.remote.board.boardLike

data class BoardDeleteLikeResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)