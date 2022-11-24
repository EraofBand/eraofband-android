package com.example.eraofband.remote.board.postBoardImg

interface PostBoardImgView {
    fun onPostImgSuccess(result : String)
    fun onPostImgFailure(code: Int, message : String)
}