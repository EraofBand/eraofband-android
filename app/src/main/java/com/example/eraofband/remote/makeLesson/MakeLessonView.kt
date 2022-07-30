package com.example.eraofband.remote.makeLesson

import com.example.eraofband.remote.makePofol.MakePofolResult

interface MakeLessonView {
    fun onMakeLessonSuccess(code: Int, result: MakeLessonResult)
    fun onMakeLessonFailure(code: Int, message: String)
}