package com.example.eraofband.remote.portfolio

interface PofolCommentView {

    // 댓글 불러오기
    fun onCommentSuccess(code:Int, result: List<PofolCommentResult>)
    fun onCommentFailure(code: Int, message: String)

    // 댓글 달기
    fun onCommentWriteSuccess(code:Int, result: List<PofolCommentWriteResult>)
    fun onCommentWriteFailure(code: Int, message: String)

    // 댓글 삭제하기
    fun onCommentDeleteSuccess(code:Int, result: String)
    fun onCommentDeleteFailure(code: Int, message: String)
}