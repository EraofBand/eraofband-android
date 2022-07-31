package com.example.eraofband.remote.user.userFollowList

interface UserFollowListView {
    fun onUserFollowListSuccess(code: Int, result: UserFollowListResult)
    fun onUserFollowListFailure(code: Int, message: String)
}