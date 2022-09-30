package com.example.eraofband.remote.block.cancelBlock

interface CancelBlockView {
    fun onCancelSuccess(result: String)
    fun onCancelFailure(code: Int, message: String)
}