package com.example.eraofband.remote.user.getOtherUser

interface GetOtherUserView {
    fun onGetSuccess(code: Int, result : GetOtherUserResult)
    fun onGetFailure(code: Int, message : String)
}