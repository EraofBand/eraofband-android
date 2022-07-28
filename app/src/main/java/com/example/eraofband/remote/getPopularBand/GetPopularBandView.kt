package com.example.eraofband.remote.getPopularBand

interface GetPopularBandView {
    fun onGetPopSuccess(result : List<GetPopularBandResult>)
    fun onGetPopFailure(code: Int, message : String)
}