package com.example.eraofband.remote.userfollowlist

import com.example.eraofband.remote.userfollow.UserFollowResponse

interface UserFollowListView {
    fun onUserFollowListSuccess(code: Int, result: UserFollowListResult)
    fun onUserFollowListFailure(code: Int, message: String)
}