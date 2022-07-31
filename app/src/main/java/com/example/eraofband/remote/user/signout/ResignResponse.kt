package com.example.eraofband.remote.user.signout

data class ResignResponse(
    val code: Int,
    val inSuccess: Boolean,
    val message: String,
    val result: String
)