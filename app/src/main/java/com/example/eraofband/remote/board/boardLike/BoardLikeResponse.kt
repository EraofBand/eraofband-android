package com.example.eraofband.remote.board.boardLike

data class BoardLikeResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: BoardLikeResult
)

data class BoardLikeResult(
    var boardLikeIdx: Int
)