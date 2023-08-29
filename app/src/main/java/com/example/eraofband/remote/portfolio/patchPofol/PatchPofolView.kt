package com.example.eraofband.remote.portfolio.patchPofol

import com.example.eraofband.remote.BasicResponse

interface PatchPofolView {
    fun onPatchUserSuccess(code: Int, result: String)
    fun onPatchUserFailure(response: BasicResponse)
}