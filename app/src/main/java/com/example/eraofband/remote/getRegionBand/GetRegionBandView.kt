package com.example.eraofband.remote.getRegionBand

interface GetRegionBandView {
    fun onGetSuccess(code: Int, result: List<GetRegionBandResult>)
    fun onGetFailure(code: Int, message: String)
}