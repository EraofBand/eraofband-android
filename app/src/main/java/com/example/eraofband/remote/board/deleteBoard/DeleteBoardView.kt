package com.example.eraofband.remote.board.deleteBoard

interface DeleteBoardView {
    fun onDeleteSuccess(result: String)
    fun onDeleteFailure(code: Int, message: String)
}