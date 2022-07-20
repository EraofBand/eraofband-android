package com.example.eraofband.remote.signout

data class ResignResponse(
    val code: Int,
    val inSuccess: Boolean,
    val message: String,
    val result: String
)