package com.example.eraofband.remote.makeBand


interface MakeBandView {
    fun onMakeSuccess(code: Int, result: MakeBandResult)
    fun onMakeFailure(code: Int, message: String)
}