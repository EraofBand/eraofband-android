package com.example.eraofband.remote.board.getMyCommentList

import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResult

data class GetMyCommentListResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ArrayList<GetMyBoardListResult>
)