package com.example.eraofband.remote.board.boardComment

import com.example.eraofband.remote.board.getBoard.GetBoardComments

interface BoardCommentView {
    fun onWriteSuccess(result: GetBoardComments)
    fun onWriteFailure(code: Int, message: String)

    fun onDeleteSuccess(result: String)
    fun onHaveReply(code: Int, message: String)
    fun onDeleteFailure(code: Int, message: String)
}