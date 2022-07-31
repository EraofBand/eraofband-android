package com.example.eraofband.remote.user.kakaologin

import com.google.gson.annotations.SerializedName

data class KakaoLoginResponse (
    @SerializedName(value = "code") var code : Int,
    @SerializedName(value = "isSuccess") var isSuccess : Boolean,
    @SerializedName(value = "message") var message : String,
    @SerializedName(value = "result") var result : LoginResult
)

data class LoginResult (
    @SerializedName(value = "jwt") var jwt : String,
    @SerializedName(value = "userIdx") var userIdx : Int
)