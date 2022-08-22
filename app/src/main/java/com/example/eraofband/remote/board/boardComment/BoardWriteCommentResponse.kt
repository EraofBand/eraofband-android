package com.example.eraofband.remote.board.boardComment

import com.example.eraofband.remote.board.getBoard.GetBoardComments

data class BoardWriteCommentResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: GetBoardComments
)