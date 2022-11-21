package com.example.eraofband.remote.portfolio.pofolLike

data class PofolLikeResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : PofolLikeResult
)

data class PofolLikeResult (
    var pofolLikeIdx : Int
)