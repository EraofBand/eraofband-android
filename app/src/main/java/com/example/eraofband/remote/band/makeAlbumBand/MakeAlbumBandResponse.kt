package com.example.eraofband.remote.band.makeAlbumBand

data class MakeAlbumBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: MakeAlbumBandResult
)

data class MakeAlbumBandResult(
    var bandAlbumIdx: Int
)