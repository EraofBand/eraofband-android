package com.example.eraofband.remote.getotheruser

interface GetOtherUserView {
    fun onGetSuccess(code: Int, result : GetOtherUserResult)
    fun onGetFailure(code: Int, message : String)
}