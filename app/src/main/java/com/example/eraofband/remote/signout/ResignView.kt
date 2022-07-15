package com.example.eraofband.remote.signout

import com.example.eraofband.remote.pofollike.PofolLikeResult

interface ResignView {
    fun onResignSuccess(code: Int, response: ResignResponse)
    fun onResignFailure(code: Int, message: String)
}