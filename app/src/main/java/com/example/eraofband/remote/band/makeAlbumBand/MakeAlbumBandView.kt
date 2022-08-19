package com.example.eraofband.remote.band.makeAlbumBand

interface MakeAlbumBandView {
    fun onMakeSuccess(result: MakeAlbumBandResult)
    fun onMakeFailure(code: Int, message : String)
}