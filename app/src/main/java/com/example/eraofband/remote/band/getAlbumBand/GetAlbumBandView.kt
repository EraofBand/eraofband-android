package com.example.eraofband.remote.band.getAlbumBand

interface GetAlbumBandView {
    fun onGetSuccess(result : List<GetAlbumBandResult>)
    fun onGetFailure(code: Int, message : String)
}