package com.example.eraofband.remote.search.getLesson

data class GetSearchLessonResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetSearchLessonResult>
)

data class GetSearchLessonResult(
    var capacity : Int,
    var lessonIdx : Int,
    var lessonImgUrl : String,
    var lessonIntroduction : String,
    var lessonRegion : String,
    var lessonTitle : String,
    var memberCount : Int
)
