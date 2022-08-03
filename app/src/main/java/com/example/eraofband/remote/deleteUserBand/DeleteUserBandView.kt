package com.example.eraofband.remote.deleteUserBand

interface DeleteUserBandView {
    fun onDeleteUserSuccess(code: Int, result: String)
    fun onDeleteUserFailure(response: DeleteUserBandResponse)
}