package com.example.eraofband.ui.main.search.lesson

import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult

interface SearchLessonInterface {
    fun initLessonRV(result: List<GetSearchLessonResult>)
}