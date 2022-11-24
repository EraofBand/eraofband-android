package com.example.eraofband.remote.lesson.applyLesson

interface ApplyLessonView {
    fun onApplyLessonSuccess(result: ApplyLessonResult)
    fun onApplyLessonFailure(code: Int, message: String)
}