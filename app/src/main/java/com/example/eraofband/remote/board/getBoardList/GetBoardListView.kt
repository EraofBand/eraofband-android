package com.example.eraofband.remote.board.getBoardList

interface GetBoardListView {
    fun onGetListSuccess(result : ArrayList<GetBoardListResult>)
    fun onGetListFailure(code: Int, message : String)
}