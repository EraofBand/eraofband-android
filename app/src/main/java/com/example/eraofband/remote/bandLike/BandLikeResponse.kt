package com.example.eraofband.remote.bandLike

data class BandLikeResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : BandLikeResult
)

data class BandLikeResult (
    var bandLikeIdx : Int
)