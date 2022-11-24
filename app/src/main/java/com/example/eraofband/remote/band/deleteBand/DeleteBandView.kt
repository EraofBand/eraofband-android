package com.example.eraofband.remote.band.deleteBand


interface DeleteBandView {
    fun onDeleteSuccess(result: String)
    fun onDeleteFailure(code: Int, message: String)
}