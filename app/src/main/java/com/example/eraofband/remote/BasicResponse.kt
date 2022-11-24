package com.example.eraofband.remote

data class BasicResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)
