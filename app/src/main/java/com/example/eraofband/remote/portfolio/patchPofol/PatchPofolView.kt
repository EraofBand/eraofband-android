package com.example.eraofband.remote.portfolio.patchPofol

import com.example.eraofband.remote.BasicResponse

interface PatchPofolView {
    fun onPatchSuccess(code: Int, result: String)
    fun onPatchFailure(response: BasicResponse)
}