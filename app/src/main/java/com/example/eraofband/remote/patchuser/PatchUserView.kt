package com.example.eraofband.remote.patchuser


interface PatchUserView {
    fun onPatchSuccess(code: Int, result : String)
    fun onPatchFailure(code: Int, message : String)
}