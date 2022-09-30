package com.example.eraofband.remote.block.getBlock

interface GetBlockView {
    fun onBlockSuccess(result: List<GetBlockResult>)
    fun onBlockFailure(code: Int, message: String)
}