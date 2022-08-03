package com.example.eraofband.remote.band.getRegionBand

interface GetRegionBandView {
    fun onGetSuccess(code: Int, result: List<GetRegionBandResult>)
    fun onGetFailure(code: Int, message: String)
}