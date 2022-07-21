package com.example.eraofband.remote.userfollow

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.signout.ResignResponse
import com.example.eraofband.remote.signout.ResignView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowService {
    private lateinit var userFollowView: UserFollowView

    fun setUserFollowView(userFollowView: UserFollowView) {
        this.userFollowView = userFollowView
    }

    fun userFollow(jwt: String, userIdx: Int) {

        val userFollowService =  NetworkModule().getRetrofit()?.create(API::class.java)

        userFollowService?.userFollow(jwt, userIdx)?.enqueue(object : Callback<UserFollowResponse> {
            override fun onResponse(
                call: Call<UserFollowResponse>,
                response: Response<UserFollowResponse>
            ) {
                Log.d("FOLLOW / SUCCESS", response.toString())

                val resp: UserFollowResponse = response.body()!!

                when (val code = resp.code) {
                    1000 -> userFollowView.onUserFollowSuccess(code, resp)
                    else -> userFollowView.onUserFollowFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<UserFollowResponse>, t: Throwable) {
                Log.d("FOLLOW / FAILURE", t.message.toString())
            }
        })
    }
}