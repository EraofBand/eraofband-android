package com.example.eraofband.remote.block.getBlock

data class GetBlockResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetBlockResult>
)

data class GetBlockResult(
    val blockChecked: Int,
    val nickName: String,
    val profileImgUrl: String,
    val userIdx: Int
)
