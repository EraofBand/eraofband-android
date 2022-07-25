package com.example.eraofband.remote.makePofol

interface MakePofolView {
    fun onMakeSuccess(code: Int, result: MakePofolResult)
    fun onMakeFailure(code: Int, message: String)
}