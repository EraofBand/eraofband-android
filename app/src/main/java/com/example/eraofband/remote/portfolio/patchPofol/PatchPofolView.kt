package com.example.eraofband.remote.portfolio.patchPofol

interface PatchPofolView {
    fun onPatchSuccess(code: Int, result: String)
    fun onPatchFailure(response: PatchPofolResponse)
}