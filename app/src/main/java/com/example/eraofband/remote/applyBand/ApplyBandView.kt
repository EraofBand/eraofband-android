package com.example.eraofband.remote.applyBand

interface ApplyBandView {
    fun onApplySuccess(result: ApplyBandResult)
    fun onApplyFailure(code: Int, message: String)
}