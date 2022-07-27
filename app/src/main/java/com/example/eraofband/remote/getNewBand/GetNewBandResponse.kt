package com.example.eraofband.remote.getNewBand

data class GetNewBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetNewBandResult>
)

data class GetNewBandResult(
    var bandIdx: Int,
    var bandImgUrl: String,
    var bandRegion: String,
    var bandTitle: String,
    var sessionNum: Int,
    var totalNum: Int
)