package com.example.eraofband.remote.board.deleteBoard

data class DeleteBoardResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)