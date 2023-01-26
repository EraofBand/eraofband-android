package com.example.eraofband.remote.user.autologin

interface AutoLoginView {
    fun onLoginSuccess(code: Int, result : String)
    fun onLoginFailure(code: Int, message : String)
}