package com.example.eraofband.remote.deleteLesson


interface DeleteLessonView {
    fun onDeleteSuccess(code: Int, result: String)
    fun onDeleteFailure(response: DeleteLessonResponse)
}