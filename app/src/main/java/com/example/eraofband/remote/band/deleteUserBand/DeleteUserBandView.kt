package com.example.eraofband.remote.band.deleteUserBand

interface DeleteUserBandView {
    fun onDeleteUserSuccess(result: String)
    fun onDeleteUserFailure(code: Int, message: String)
}