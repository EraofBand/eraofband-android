package com.example.eraofband.remote.getMyPage

import com.example.eraofband.remote.getuser.GetMyPageResult

interface GetMyPageView {
    fun onGetSuccess(code: Int, result : GetMyPageResult)
    fun onGetFailure(code: Int, message : String)
}