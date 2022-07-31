package com.example.eraofband.remote.user.signout

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResignService {
    private lateinit var resignView: ResignView

    fun setResignView(resignView: ResignView) {
        this.resignView = resignView
    }

    fun resign(jwt: String, userIdx: Int) {

        val resignService =  NetworkModule().getRetrofit()?.create(API::class.java)

        resignService?.resign(jwt, userIdx)?.enqueue(object : Callback<ResignResponse> {
            override fun onResponse(call: Call<ResignResponse>, response: Response<ResignResponse>) {
                // 응답이 왔을 때 처리
                Log.d("SIGNOUT / SUCCESS", response.toString())

                val resp : ResignResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> resignView.onResignSuccess(code, resp)  // 성공
                    else -> resignView.onResignFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<ResignResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("SIGNOUT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }
}