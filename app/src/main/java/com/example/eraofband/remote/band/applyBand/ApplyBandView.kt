package com.example.eraofband.remote.band.applyBand

interface ApplyBandView {
    fun onApplySuccess(result: ApplyBandResult)
    fun onDuplicateApply(code: Int, message: String)
    fun onApplyFailure(code: Int, message: String)
}