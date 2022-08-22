package com.example.eraofband.remote.board.boardComment

data class BoardDeleteCommentResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)