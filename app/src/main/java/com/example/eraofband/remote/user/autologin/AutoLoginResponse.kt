package com.example.eraofband.remote.user.autologin

data class AutoLoginResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : String
)
