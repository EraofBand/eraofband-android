package com.example.eraofband.remote.lesson.lessonLike

data class LessonLikeResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: LessonLikeResult
)

data class LessonLikeResult (
var lessonLikeIdx: Int
)