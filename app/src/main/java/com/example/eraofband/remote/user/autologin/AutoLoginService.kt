package com.example.eraofband.remote.user.autologin

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutoLoginService {
    private lateinit var autoLoginView : AutoLoginView

    fun setAutoLoginView(autoLoginView: AutoLoginView) {
        this.autoLoginView = autoLoginView
    }

    fun autoLogin(token : String?, userIdx: Int) {

        val autoLoginService =  NetworkModule().getRetrofit()?.create(API::class.java)

        autoLoginService?.autoLogin(token!!, userIdx)?.enqueue(object : Callback<AutoLoginResponse> {
            override fun onResponse(call: Call<AutoLoginResponse>, response: Response<AutoLoginResponse>) {
                // 응답이 왔을 때 처리
                Log.d("LOGIN / SUCCESS", response.toString())

                val resp : AutoLoginResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> autoLoginView.onLoginSuccess(code, resp.result)  // 성공
                    else -> autoLoginView.onLoginFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<AutoLoginResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("LOGIN / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

}