package com.example.eraofband.remote.lesson.getLikeLessonList

interface GetLessonLikeListView {
    fun onGetLessonLikeListSuccess(code: Int, result: List<GetLessonLikeListResult>)
    fun onGetLessonLikeListFailure(code: Int, message: String)
}