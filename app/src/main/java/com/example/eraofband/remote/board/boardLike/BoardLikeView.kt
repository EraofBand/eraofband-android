package com.example.eraofband.remote.board.boardLike

interface BoardLikeView {
    fun onLikeSuccess(result: BoardLikeResult)
    fun onLikeFailure(code: Int, message: String)

    fun onDeleteLikeSuccess(result: String)
    fun onDeleteLikeFailure(code: Int, message: String)
}