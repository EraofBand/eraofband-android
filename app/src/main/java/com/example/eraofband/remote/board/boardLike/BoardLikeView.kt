package com.example.eraofband.remote.board.boardLike

interface BoardLikeView {
    fun onLikeSuccess(result: BoardLikeResult)
    fun onLikeFailure(code: Int, result: String)

//    fun onDeleteLikeSuccess(code: Int, result: String)
}