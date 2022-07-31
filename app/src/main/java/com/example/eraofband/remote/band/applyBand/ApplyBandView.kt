package com.example.eraofband.remote.band.applyBand

interface ApplyBandView {
    fun onApplySuccess(result: ApplyBandResult)
    fun onApplyFailure(code: Int, message: String)
}