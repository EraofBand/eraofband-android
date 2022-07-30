package com.example.eraofband.remote.applyDecision

interface ApplyDecisionView {
    fun onAcceptSuccess(result: String)
    fun onAcceptFailure(code: Int, message: String)

    fun onRejectSuccess(result: String)
    fun onRejectFailure(code: Int, message: String)
}