package com.example.eraofband.remote.portfolio.makePofol

data class MakePofolResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: MakePofolResult
)

data class MakePofolResult(
    var pofolIdx: Int
)