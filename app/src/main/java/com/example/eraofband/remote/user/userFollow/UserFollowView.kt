package com.example.eraofband.remote.user.userFollow

interface UserFollowView {
    fun onUserFollowSuccess(code: Int, response: UserFollowResponse)
    fun onUserFollowFailure(code: Int, message: String)
}