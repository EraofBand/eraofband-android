package com.example.eraofband.remote.signout

interface ResignView {
    fun onResignSuccess(code: Int, response: ResignResponse)
    fun onResignFailure(code: Int, message: String)
}