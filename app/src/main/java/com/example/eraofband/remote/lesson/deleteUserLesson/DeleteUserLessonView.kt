package com.example.eraofband.remote.lesson.deleteUserLesson

interface DeleteUserLessonView {
    fun onDeleteUserSuccess(result: String)
    fun onDeleteUserFailure(code: Int, message: String)
}