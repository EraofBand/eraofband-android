package com.example.eraofband.remote.band.getLikedBand

interface GetLikedBandView {
    fun onGetSuccess(result: List<GetLikedBandResult>)
    fun onGetFailure(code: Int, message: String)
}