package com.example.eraofband.remote.board.postBoard

data class PostBoardResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: PostBoardResult
)

data class PostBoardResult(
    var boardIdx : Int
    )
