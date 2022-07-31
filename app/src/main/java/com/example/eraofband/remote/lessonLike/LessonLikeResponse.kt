package com.example.eraofband.remote.lessonLike

data class LessonLikeResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: LessonLikeResult
)

data class LessonLikeResult (
var lessonLikeIdx: Int
)

data class LessonLikeDeleteResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)