package com.example.eraofband.remote.lesson.patchLesson

data class PatchLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)