package com.example.eraofband.remote.deleteUserLesson

interface DeleteUserLessonView {
    fun onDeleteUserSuccess(code: Int, result: String)
    fun onDeleteUserFailure(response: DeleteUserLessonResponse)
}