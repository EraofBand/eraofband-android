package com.example.eraofband.remote.user.getMyPage

interface GetMyPageView {
    fun onGetSuccess(code: Int, result : GetMyPageResult)
    fun onGetFailure(code: Int, message : String)
}