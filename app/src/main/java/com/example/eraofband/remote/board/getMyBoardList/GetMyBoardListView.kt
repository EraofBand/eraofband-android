package com.example.eraofband.remote.board.getMyBoardList

interface GetMyBoardListView {
    fun onGetListSuccess(result : ArrayList<GetMyBoardListResult>)
    fun onGetListFailure(code: Int, message : String)
}