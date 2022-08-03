package com.example.eraofband.remote.deleteBand


interface DeleteBandView {
    fun onDeleteSuccess(code: Int, result: String)
    fun onDeleteFailure(response: DeleteBandResponse)
}