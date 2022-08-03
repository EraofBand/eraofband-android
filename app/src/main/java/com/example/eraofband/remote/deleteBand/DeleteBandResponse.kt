package com.example.eraofband.remote.deleteBand

data class DeleteBandResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
    )