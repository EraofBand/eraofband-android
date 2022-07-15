package com.example.eraofband.remote.checkUser

interface CheckUserView {
    fun onCheckSuccess(message: String, result : CheckUserResult)
    fun onCheckFailure(code : Int, message : String)
}