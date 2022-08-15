package com.example.eraofband.remote.portfolio.getPofol

interface GetOtherPofolView {
    fun onGetTotalSuccess(result : List<GetPofolResult>)
    fun onGetTotalFailure(code: Int, message : String)

    fun onGetFollowSuccess(result : List<GetPofolResult>)
    fun onGetFollowFailure(code: Int, message : String)
}