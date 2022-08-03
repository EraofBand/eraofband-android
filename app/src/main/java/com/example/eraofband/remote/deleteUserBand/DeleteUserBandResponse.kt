package com.example.eraofband.remote.deleteUserBand

data class DeleteUserBandResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)