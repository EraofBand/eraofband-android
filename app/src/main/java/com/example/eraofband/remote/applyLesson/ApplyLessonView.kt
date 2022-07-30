package com.example.eraofband.remote.applyLesson

import com.example.eraofband.remote.applyBand.ApplyBandResult

interface ApplyLessonView {
    fun onApplyLessonSuccess(result: ApplyLessonResult)
    fun onApplyLessonFailure(code: Int, message: String)
}