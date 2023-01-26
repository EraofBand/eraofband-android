package com.example.eraofband.remote.user.refreshjwt

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefreshJwtService {
    private lateinit var refreshJwtView : RefreshJwtView

    fun setAutoLoginView(refreshJwtView: RefreshJwtView) {
        this.refreshJwtView = refreshJwtView
    }

    fun refreshJwt(refreshToken : String, userIdx: Int) {

        val refreshJwtService =  NetworkModule().getRetrofit()?.create(API::class.java)

        refreshJwtService?.refreshJwt(refreshToken, userIdx)?.enqueue(object : Callback<RefreshJwtResponse> {
            override fun onResponse(call: Call<RefreshJwtResponse>, response: Response<RefreshJwtResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH / SUCCESS", response.toString())

                val resp : RefreshJwtResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> refreshJwtView.onPatchSuccess(code, resp.result)  // 성공
                    else -> refreshJwtView.onPatchFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<RefreshJwtResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

}