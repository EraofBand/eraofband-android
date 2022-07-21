package com.example.eraofband.remote.patchPofol

interface PatchPofolView {
    fun onPatchSuccess(code: Int, result: String)
    fun onPatchFailure(response: PatchPofolResponse)
}