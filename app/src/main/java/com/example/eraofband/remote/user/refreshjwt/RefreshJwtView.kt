package com.example.eraofband.remote.user.refreshjwt

interface RefreshJwtView {
    fun onPatchSuccess(code: Int, result: RefreshResult)
    fun onPatchFailure(code: Int, message : String)
}