package com.example.eraofband.remote.portfolio.makePofol

interface MakePofolView {
    fun onMakeSuccess(code: Int, result: MakePofolResult)
    fun onMakeFailure(code: Int, message: String)
}