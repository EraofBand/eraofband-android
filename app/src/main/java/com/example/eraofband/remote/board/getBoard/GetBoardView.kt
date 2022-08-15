package com.example.eraofband.remote.board.getBoard

interface GetBoardView {
    fun onGetSuccess(result: GetBoardResult)
    fun onGetFailure(code: Int, message: String)
}