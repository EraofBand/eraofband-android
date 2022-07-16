package com.example.eraofband.remote.getMyPage

interface GetMyPageView {
    fun onGetSuccess(code: Int, result : GetMyPageResult)
    fun onGetFailure(code: Int, message : String)
}