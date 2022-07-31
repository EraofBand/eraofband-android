package com.example.eraofband.remote.getLikedBand

data class GetLikedBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetLikedBandResult>
)

data class GetLikedBandResult(
    var bandIdx: Int,
    var bandImgUrl: String,
    var bandIntroduction: String,
    var bandRegion: String,
    var bandTitle: String,
    var capacity: Int,
    var memberCount: Int
)