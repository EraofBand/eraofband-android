package com.example.eraofband.remote.band.applyDecision

data class RejectApplyResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)