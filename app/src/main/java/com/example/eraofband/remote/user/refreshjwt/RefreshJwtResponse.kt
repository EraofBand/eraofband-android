package com.example.eraofband.remote.user.refreshjwt

data class RefreshJwtResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : RefreshResult
)

data class RefreshResult (
    var expiration: Long,
    var jwt: String
)
