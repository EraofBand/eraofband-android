package com.example.eraofband.remote.lesson.makeLesson

interface MakeLessonView {
    fun onMakeLessonSuccess(code: Int, result: MakeLessonResult)
    fun onMakeLessonFailure(code: Int, message: String)
}