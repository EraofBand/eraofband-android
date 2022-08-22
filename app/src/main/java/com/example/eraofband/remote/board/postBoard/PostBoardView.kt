package com.example.eraofband.remote.board.postBoard

interface PostBoardView {
    fun onPostSuccess(code: Int, result: PostBoardResult)
    fun onPostFailure(code: Int, message : String)
}