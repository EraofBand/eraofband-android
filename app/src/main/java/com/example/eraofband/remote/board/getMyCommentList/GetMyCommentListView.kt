package com.example.eraofband.remote.board.getMyCommentList

import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResult

interface GetMyCommentListView {
    fun onGetCListSuccess(result : ArrayList<GetMyBoardListResult>)
    fun onGetCListFailure(code: Int, message : String)
}