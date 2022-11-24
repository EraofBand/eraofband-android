package com.example.eraofband.remote.lesson.deleteLesson


interface DeleteLessonView {
    fun onDeleteSuccess(result: String)
    fun onDeleteFailure(code: Int, message: String)
}