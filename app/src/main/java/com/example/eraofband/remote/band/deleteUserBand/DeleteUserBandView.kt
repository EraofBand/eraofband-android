package com.example.eraofband.remote.band.deleteUserBand

interface DeleteUserBandView {
    fun onDeleteUserSuccess(code: Int, result: String)
    fun onDeleteUserFailure(response: DeleteUserBandResponse)
}