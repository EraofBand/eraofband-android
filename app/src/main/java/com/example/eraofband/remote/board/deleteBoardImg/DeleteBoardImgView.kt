package com.example.eraofband.remote.board.deleteBoardImg

interface DeleteBoardImgView {
    fun onDeleteBoardImgSuccess(result : String)
    fun onDeleteBoardImgFailure(code: Int, message : String)
}