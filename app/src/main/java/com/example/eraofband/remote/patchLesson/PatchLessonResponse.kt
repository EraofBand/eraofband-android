package com.example.eraofband.remote.patchLesson

data class PatchLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)