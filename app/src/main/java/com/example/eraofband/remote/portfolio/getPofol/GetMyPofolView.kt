package com.example.eraofband.remote.portfolio.getPofol

interface GetMyPofolView {
    fun onGetSuccess(result : List<GetPofolResult>)
    fun onGetFailure(code: Int, message : String)
}