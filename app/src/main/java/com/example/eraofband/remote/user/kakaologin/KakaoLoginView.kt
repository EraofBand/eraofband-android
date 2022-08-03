package com.example.eraofband.remote.user.kakaologin

interface KakaoLoginView {
    fun onLoginSuccess(code: Int, result : LoginResult)
    fun onLoginFailure(code: Int, message : String)
}