package com.example.eraofband.remote.userunfollow

import com.example.eraofband.remote.userfollowlist.UserFollowListResponse

interface UserUnfollowView {
    fun onUserUnfollowSuccess(code: Int, response: UserUnfollowResponse)
    fun onUserUnfollowFailure(code: Int, message: String)
}