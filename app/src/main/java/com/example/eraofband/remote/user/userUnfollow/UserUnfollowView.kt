package com.example.eraofband.remote.user.userUnfollow

import com.example.eraofband.remote.BasicResponse

interface UserUnfollowView {
    fun onUserUnfollowSuccess(code: Int, response: BasicResponse)
    fun onUserUnfollowFailure(code: Int, message: String)
}