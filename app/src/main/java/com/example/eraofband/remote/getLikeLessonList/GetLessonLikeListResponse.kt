package com.example.eraofband.remote.getLikeLessonList

data class GetLessonLikeListResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetLessonLikeListResult>
)

data class GetLessonLikeListResult(
    var capacity: Int,
    var lessonIdx: Int,
    var lessonImgUrl: String,
    var lessonIntroduction: String,
    var lessonRegion: String,
    var lessonTitle: String,
    var memberCount: Int
)