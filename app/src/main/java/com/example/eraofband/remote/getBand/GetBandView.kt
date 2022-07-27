package com.example.eraofband.remote.getBand

interface GetBandView {
    fun onGetSuccess(result : GetBandResult)
    fun onGetFailure(code: Int, message : String)
}