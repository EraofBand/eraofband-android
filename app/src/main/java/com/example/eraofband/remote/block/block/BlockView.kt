package com.example.eraofband.remote.block.block

interface BlockView {
    fun onBlockSuccess(result: String)
    fun onBlockFailure(code: Int, message: String)
}