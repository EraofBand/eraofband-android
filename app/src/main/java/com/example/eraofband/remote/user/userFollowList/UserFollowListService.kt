package com.example.eraofband.remote.user.userFollowList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowListService {
    private lateinit var userFollowListView: UserFollowListView

    fun setUserFollowListView(userFollowListView: UserFollowListView) {
        this.userFollowListView = userFollowListView
    }

    fun userFollowList(userIdx: Int) {

        val userFollowListService =  NetworkModule().getRetrofit()?.create(API::class.java)

        userFollowListService?.userFollowList(userIdx)?.enqueue(object : Callback<UserFollowListResponse> {

            override fun onResponse(
                call: Call<UserFollowListResponse>,
                response: Response<UserFollowListResponse>
            ) {
                Log.d("FOLLOWLIST / SUCCESS", response.toString())

                val resp: UserFollowListResponse = response.body()!!

                when (val code = resp.code) {
                    1000 -> userFollowListView.onUserFollowListSuccess(code, resp.result)
                    else -> userFollowListView.onUserFollowListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<UserFollowListResponse>, t: Throwable) {
                Log.d("FOLLOWLIST / FAILURE", t.message.toString())
            }
        })
    }
}