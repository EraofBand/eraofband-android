package com.example.eraofband.remote.getNewBand

interface GetNewBandView {
    fun onGetNewSuccess(result : List<GetNewBandResult>)
    fun onGetNewFailure(code: Int, message : String)
}