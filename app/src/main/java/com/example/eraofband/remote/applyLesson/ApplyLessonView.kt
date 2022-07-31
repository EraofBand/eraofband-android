package com.example.eraofband.remote.applyLesson

interface ApplyLessonView {
    fun onApplyLessonSuccess(result: ApplyLessonResult)
    fun onApplyLessonFailure(code: Int, message: String)
}