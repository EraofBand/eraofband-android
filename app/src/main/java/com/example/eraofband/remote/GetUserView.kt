package com.example.eraofband.remote

interface GetUserView {
    fun onGetSuccess(code: Int, result : GetUserResult)
    fun onGetFailure(code: Int, message : String)
}