package com.example.eraofband.remote.user.userUnfollow

data class UserUnfollowResponse (
    val code: Int,
    val onSuccess: Boolean,
    val message: String,
    val result: String
)