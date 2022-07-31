package com.example.eraofband.remote.user.userUnfollow

interface UserUnfollowView {
    fun onUserUnfollowSuccess(code: Int, response: UserUnfollowResponse)
    fun onUserUnfollowFailure(code: Int, message: String)
}