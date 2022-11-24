package com.example.eraofband.remote.user.kakaologin

data class KakaoLoginResponse (
    var code : Int,
    var isSuccess : Boolean,
    var message : String,
    var result : LoginResult
)

data class LoginResult (
    var jwt : String,
    var userIdx : Int
)