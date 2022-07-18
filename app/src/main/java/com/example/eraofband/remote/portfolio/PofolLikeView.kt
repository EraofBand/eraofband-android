package com.example.eraofband.remote.portfolio

interface PofolLikeView {
    // 좋아요
    fun onLikeSuccess(code: Int, result : PofolLikeResult)
    fun onLikeFailure(code: Int, message : String)

    // 좋아요 취소
    fun onDeleteLikeSuccess(code: Int, result : String)
    fun onDeleteLikeFailure(code: Int, message : String)
}