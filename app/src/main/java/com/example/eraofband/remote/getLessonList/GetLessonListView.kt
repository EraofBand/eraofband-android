package com.example.eraofband.remote.getLessonList

interface GetLessonListView {
    fun onGetLessonListSuccess(code: Int, result: List<GetLessonListResult>)
    fun onGetLessonListFailure(code: Int, message: String)
}