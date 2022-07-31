package com.example.eraofband.remote.band.getPopularBand

interface GetPopularBandView {
    fun onGetPopSuccess(result : List<GetPopularBandResult>)
    fun onGetPopFailure(code: Int, message : String)
}