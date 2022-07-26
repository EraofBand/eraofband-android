package com.example.eraofband.remote.getNewBand

interface GetNewBandView {
    fun onGetSuccess(result : List<GetNewBandResult>)
    fun onGetFailure(code: Int, message : String)
}