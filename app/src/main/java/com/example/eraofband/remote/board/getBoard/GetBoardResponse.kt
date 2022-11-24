package com.example.eraofband.remote.board.getBoard

data class GetBoardResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: GetBoardResult
)

data class GetBoardResult(
    var boardIdx: Int,
    var boardLikeCount: Int,
    var category: Int,
    var commentCount: Int,
    var content: String,
    var getBoardComments: List<GetBoardComments>,
    var getBoardImgs: List<GetBoardImgs>,
    var likeOrNot: String,
    var nickName: String,
    var profileImgUrl: String,
    var title: String,
    var updatedAt: String,
    var userIdx: Int,
    var views: Int
)

data class GetBoardComments(
    var boardCommentIdx: Int,
    var boardIdx: Int,
    var classNum: Int,
    var commentStatus: String,
    var content: String,
    var groupNum: Int,
    var hasReply: Int,
    var nickName: String,
    var profileImgUrl: String,
    var updatedAt: String,
    var userIdx: Int,
    var userStatus: String
)

data class GetBoardImgs(
    var boardImgIdx: Int,
    var imgUrl: String
)