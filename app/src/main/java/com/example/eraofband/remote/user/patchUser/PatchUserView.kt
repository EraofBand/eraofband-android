package com.example.eraofband.remote.user.patchUser


interface PatchUserView {
    fun onPatchSuccess(code: Int, result : PatchUserResult)
    fun onPatchFailure(code: Int, message : String)
}