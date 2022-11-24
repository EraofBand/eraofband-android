package com.example.eraofband.remote.board.postBoard

interface PostBoardView {
    fun onPostSuccess(result: String)
    fun onPostFailure(code: Int, message : String)
}