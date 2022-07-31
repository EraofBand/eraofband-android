package com.example.eraofband.remote.user.checkUser

interface CheckUserView {
    fun onCheckSuccess(result : CheckUserResult)
    fun onCheckFailure(code : Int, message : String)
}