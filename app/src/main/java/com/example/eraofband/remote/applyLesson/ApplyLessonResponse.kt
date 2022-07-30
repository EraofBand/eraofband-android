package com.example.eraofband.remote.applyLesson

data class ApplyLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ApplyLessonResult
)

data class ApplyLessonResult(
    var LessonIdx: Int
)