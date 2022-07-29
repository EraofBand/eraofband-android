package com.example.eraofband.remote.bandLike

interface BandLikeView {
    // 좋아요
    fun onLikeSuccess(result : BandLikeResult)
    fun onLikeFailure(code: Int, message : String)

    // 좋아요 취소
    fun onDeleteLikeSuccess(result : String)
    fun onDeleteLikeFailure(code: Int, message : String)
}