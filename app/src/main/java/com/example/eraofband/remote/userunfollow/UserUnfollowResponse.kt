package com.example.eraofband.remote.userunfollow

import com.example.eraofband.remote.userfollowlist.UserFollowListResult

data class UserUnfollowResponse (
    val code: Int,
    val onSuccess: Boolean,
    val message: String,
    val result: String
)