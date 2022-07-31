package com.example.eraofband.remote.lesson.makeLesson

data class MakeLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: MakeLessonResult
)

data class MakeLessonResult(
    var lessonIdx: Int
)