package com.example.eraofband.remote.lesson.deleteLesson


interface DeleteLessonView {
    fun onDeleteSuccess(code: Int, result: String)
    fun onDeleteFailure(response: DeleteLessonResponse)
}