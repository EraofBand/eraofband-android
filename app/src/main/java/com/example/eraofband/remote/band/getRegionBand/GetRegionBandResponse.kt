package com.example.eraofband.remote.band.getRegionBand

data class GetRegionBandResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetRegionBandResult>
        )

data class GetRegionBandResult (
    var bandIdx: Int,
    var bandImgUrl : String,
    var bandIntroduction : String,
    var bandRegion : String,
    var bandTitle: String,
    var capacity: Int,
    var memberCount: Int
        )