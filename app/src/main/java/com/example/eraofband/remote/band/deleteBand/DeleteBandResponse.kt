package com.example.eraofband.remote.band.deleteBand

data class DeleteBandResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
    )