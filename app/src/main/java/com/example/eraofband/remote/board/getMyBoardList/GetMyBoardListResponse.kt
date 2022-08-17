package com.example.eraofband.remote.board.getMyBoardList

data class GetMyBoardListResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ArrayList<GetMyBoardListResult>
)

data class GetMyBoardListResult (
    var boardIdx: Int,
    var boardLikeCount: Int,
    var category: Int,
    var commentCount: Int,
    var title: String,
    var updatedAt: String,
    var views: Int
)