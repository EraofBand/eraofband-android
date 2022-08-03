package com.example.eraofband.remote.lesson.lessonLike

interface LessonLikeView {
    // 좋아요
    fun onLikeSuccess(code: Int, result : LessonLikeResult)
    fun onLikeFailure(code: Int, message : String)

    // 좋아요 취소
    fun onDeleteLikeSuccess(code: Int, result : String)
    fun onDeleteLikeFailure(code: Int, message : String)
}