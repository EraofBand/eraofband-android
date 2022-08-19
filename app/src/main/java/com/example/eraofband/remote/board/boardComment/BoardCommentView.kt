package com.example.eraofband.remote.board.boardComment

import com.example.eraofband.remote.board.getBoard.GetBoardComments

interface BoardCommentView {
    fun onWriteCommentSuccess(result: GetBoardComments)
    fun onWriteCommentFailure(code: Int, message: String)

    fun onDeleteCommentSuccess(result: String)
    fun onHaveReply(code: Int, message: String)
    fun onDeleteCommentFailure(code: Int, message: String)
}