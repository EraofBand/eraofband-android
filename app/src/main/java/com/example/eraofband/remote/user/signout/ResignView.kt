package com.example.eraofband.remote.user.signout

import com.example.eraofband.remote.BasicResponse

interface ResignView {
    fun onResignSuccess(code: Int, response: BasicResponse)
    fun onResignFailure(code: Int, message: String)
}