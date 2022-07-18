package com.example.eraofband.remote.patchSession

interface PatchSessionView {
    fun onPatchSessionSuccess(result : String)
    fun onPatchSessionFailure(code : Int, message : String)
}