package com.example.eraofband.remote.getuser

import android.util.Log
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetUserService {
    private lateinit var getUserView: GetUserView

    fun setUserView(getUserView: GetUserView) {
        this.getUserView = getUserView
    }

    fun getUser(userIdx: Int) {

        val getUserService =  NetworkModule().getRetrofit()?.create(GetUserInterface::class.java)

        getUserService?.getUser(userIdx)?.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET / SUCCESS", response.toString())

                val resp : GetUserResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getUserView.onGetSuccess(code, resp.result)  // 성공
                    else -> getUserView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}