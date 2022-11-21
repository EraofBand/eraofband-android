package com.example.eraofband.remote.block.getBlock

interface GetBlockView {
    fun onGetSuccess(result: List<GetBlockResult>)
    fun onGetFailure(code: Int, message: String)
}