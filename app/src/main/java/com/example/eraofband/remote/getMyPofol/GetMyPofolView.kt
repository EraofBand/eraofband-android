package com.example.eraofband.remote.getMyPofol

interface GetMyPofolView {
    fun onGetSuccess(result : List<GetMyPofolResult>)
    fun onGetFailure(code: Int, message : String)
}