package com.example.eraofband.remote.band.applyBand

data class ApplyBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: ApplyBandResult
)

data class ApplyBandResult(
    var bandUserIdx: Int
)