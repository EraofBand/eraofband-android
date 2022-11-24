package com.example.eraofband.remote.user.userUnfollow

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserUnfollowService {
    private lateinit var userUnfollowView: UserUnfollowView

    fun setUserUnfollowView(userUnfollowView: UserUnfollowView) {
        this.userUnfollowView = userUnfollowView
    }

    fun userUnfollow(jwt: String, userIdx: Int) {

        val userUnfollowService =  NetworkModule().getRetrofit()?.create(API::class.java)

        userUnfollowService?.userUnfollow(jwt, userIdx)?.enqueue(object : Callback<BasicResponse> {

            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                Log.d("UNFOLLOW / SUCCESS", response.toString())

                val resp: BasicResponse = response.body()!!

                when (val code = resp.code) {
                    1000 -> userUnfollowView.onUserUnfollowSuccess(code, resp)
                    else -> userUnfollowView.onUserUnfollowFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("UNFOLLOW / FAILURE", t.message.toString())
            }
        })
    }
}