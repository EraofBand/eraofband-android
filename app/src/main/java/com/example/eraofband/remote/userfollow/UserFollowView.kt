package com.example.eraofband.remote.userfollow

import com.example.eraofband.remote.signout.ResignResponse

interface UserFollowView {
    fun onUserFollowSuccess(code: Int, response: UserFollowResponse)
    fun onUserFollowFailure(code: Int, message: String)
}