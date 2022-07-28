package com.example.eraofband.remote.getPopularBand

data class GetPopularBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetPopularBandResult>
)

data class GetPopularBandResult(
    var bandIdx: Int,
    var bandImgUrl: String,
    var bandIntroduction: String,
    var bandTitle: String,
)