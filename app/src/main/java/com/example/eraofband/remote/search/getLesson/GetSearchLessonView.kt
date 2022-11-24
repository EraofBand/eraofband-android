package com.example.eraofband.remote.search.getLesson


interface GetSearchLessonView {
    fun onGetSearchLessonSuccess(result : List<GetSearchLessonResult>)
    fun onGetSearchLessonFailure(code: Int, message : String)
}