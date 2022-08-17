package com.example.eraofband.remote.board.getBoardList

data class GetBoardListResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ArrayList<GetBoardListResult>
)

data class GetBoardListResult (
    var boardIdx: Int,
    var boardLikeCount: Int,
    var category: Int,
    var commentCount: Int,
    var content: String,
    var imgUrl: String,
    var nickName: String,
    var title: String,
    var updatedAt: String,
    var userIdx: Int,
    var views: Int
)