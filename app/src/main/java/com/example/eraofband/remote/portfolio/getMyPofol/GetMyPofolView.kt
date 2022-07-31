package com.example.eraofband.remote.portfolio.getMyPofol

interface GetMyPofolView {
    fun onGetSuccess(result : List<GetMyPofolResult>)
    fun onGetFailure(code: Int, message : String)
}