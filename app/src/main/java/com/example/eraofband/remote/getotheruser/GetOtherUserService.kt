package com.example.eraofband.remote.getotheruser

import android.util.Log
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetOtherUserService {
    private lateinit var getOtherUserView: GetOtherUserView

    fun setOtherUserView(getOtherUserView: GetOtherUserView) {
        this.getOtherUserView = getOtherUserView
    }

    fun getOtherUser(jwt : String, userIdx : Int){
        val getOtherUserService = NetworkModule().getRetrofit()?.create(GetOtherUserInterface::class.java)

        getOtherUserService?.getUser(jwt, userIdx)?.enqueue(object : Callback<GetOtherUserResponse> {
            override fun onResponse(
                call: Call<GetOtherUserResponse>,
                response: Response<GetOtherUserResponse>
            ) {
                Log.d("GET OTHER / SUCCESS", response.toString())

                val resp : GetOtherUserResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getOtherUserView.onGetSuccess(code, resp.result)  // 성공
                    else -> getOtherUserView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetOtherUserResponse>, t: Throwable) {
                Log.d("GET OTHER / FAILURE", t.message.toString())
            }
        })
    }
}