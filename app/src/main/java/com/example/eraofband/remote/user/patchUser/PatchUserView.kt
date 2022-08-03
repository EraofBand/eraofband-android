package com.example.eraofband.remote.user.patchUser


interface PatchUserView {
    fun onPatchSuccess(code: Int, result : String)
    fun onPatchFailure(code: Int, message : String)
}