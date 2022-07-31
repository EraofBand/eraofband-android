package com.example.eraofband.remote.getLikedBand

interface GetLikedBandView {
    fun onGetSuccess(result: List<GetLikedBandResult>)
    fun onGetFailure(code: Int, message: String)
}