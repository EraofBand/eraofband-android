package com.example.eraofband.remote.lesson.deleteUserLesson

data class DeleteUserLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
    )