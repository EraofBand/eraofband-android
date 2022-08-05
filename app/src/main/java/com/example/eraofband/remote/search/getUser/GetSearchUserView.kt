package com.example.eraofband.remote.search.getUser

interface GetSearchUserView {
    fun onGetSearchUserSuccess(result : List<GetSearchUserResult>)
    fun onGetSearchUserFailure(code: Int, message : String)
}