package com.example.eraofband.remote.user.patchUser


interface PatchUserView {
    fun onPatchUserSuccess(code: Int, result : String)
    fun onPatchUserFailure(code: Int, message : String)
}