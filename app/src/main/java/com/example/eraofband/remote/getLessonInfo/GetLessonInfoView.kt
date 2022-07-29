package com.example.eraofband.remote.getLessonInfo

interface GetLessonInfoView {
    fun onGetLessonInfoSuccess(code: Int, result: GetLessonInfoResult)
    fun onGetLessonInfoFailure(code: Int, message: String)
}