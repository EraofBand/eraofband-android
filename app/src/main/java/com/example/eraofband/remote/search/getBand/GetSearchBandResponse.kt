package com.example.eraofband.remote.search.getBand

data class GetSearchBandResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetSearchBandResult>
        )

data class GetSearchBandResult(
    var bandIdx : Int,
    var bandImgUrl : String,
    var bandIntroduction : String,
    var bandRegion : String,
    var bandTitle : String,
    var capacity : Int,
    var memberCount : Int
)