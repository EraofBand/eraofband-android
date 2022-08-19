package com.example.eraofband.remote.band.getAlbumBand

import com.example.eraofband.remote.band.getBand.GetBandResult

data class GetAlbumBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: List<GetAlbumBandResult>
)

data class GetAlbumBandResult(
    var albumDate: String,
    var albumImgUrl: String,
    var albumTitle: String,
    var bandAlbumIdx: Int
)