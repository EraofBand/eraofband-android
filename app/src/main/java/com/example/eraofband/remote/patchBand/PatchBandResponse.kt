package com.example.eraofband.remote.patchBand

data class PatchBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)