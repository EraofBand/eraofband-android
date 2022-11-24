package com.example.eraofband.remote.band.makeBand


data class MakeBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: MakeBandResult
)

data class MakeBandResult(
    var bandIdx: Int
)