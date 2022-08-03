package com.example.eraofband.remote.band.patchBand

interface PatchBandView {
    fun onPatchSuccess(code: Int, result: String)
    fun onPatchFailure(code: Int, message: String)
}