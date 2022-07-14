package com.example.eraofband.remote.checkUser

interface CheckUserView {
    fun onCheckSuccess(result : CheckUserResult)
    fun onCheckFailure(code : Int, message : String)
}