package com.example.eraofband.remote.lesson.deleteLesson

data class DeleteLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)
