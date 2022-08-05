package com.example.eraofband.remote.search.getBand


interface GetSearchBandView {
    fun onGetSearchBandSuccess(result : List<GetSearchBandResult>)
    fun onGetSearchBandFailure(code: Int, message : String)
}