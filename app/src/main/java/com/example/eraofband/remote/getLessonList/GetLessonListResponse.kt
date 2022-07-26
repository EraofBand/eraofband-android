package com.example.eraofband.remote.getLessonList

data class GetLessonListResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetLessonListResult>
)

data class GetLessonListResult(
    var capacity: Int,
    var lessonIdx: Int,
    var lessonImgUrl: String,
    var lessonIntroduction: String,
    var lessonRegion: String,
    var lessonTitle: String,
    var memberCount: Int
)