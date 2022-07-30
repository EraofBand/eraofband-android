package com.example.eraofband.remote.patchLesson

interface PatchLessonView {
    fun onPatchLessonSuccess(code: Int, result: String)
    fun onPatchLessonFailure(code: Int, message: String)
}