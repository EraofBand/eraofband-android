package com.example.eraofband.remote.band.getBand

interface GetBandView {
    fun onGetSuccess(result : GetBandResult)
    fun onGetFailure(code: Int, message : String)
}