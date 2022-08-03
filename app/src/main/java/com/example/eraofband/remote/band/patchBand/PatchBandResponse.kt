package com.example.eraofband.remote.band.patchBand

data class PatchBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)