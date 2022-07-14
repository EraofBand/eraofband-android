package com.example.eraofband.remote.kakaologin

import android.util.Log
import com.example.eraofband.data.User
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLoginService {
    private lateinit var loginView : KakaoLoginView

    fun setLoginView(loginView: KakaoLoginView) {
        this.loginView = loginView
    }

    fun login(user: User, token : String?) {

        val loginService =  NetworkModule().getRetrofit()?.create(KakaoLoginInterface::class.java)

        loginService?.kakaoLogin(user, token!!)?.enqueue(object : Callback<KakaoLoginResponse> {
            override fun onResponse(call: Call<KakaoLoginResponse>, response: Response<KakaoLoginResponse>) {
                // 응답이 왔을 때 처리
                Log.d("LOGIN / SUCCESS", response.toString())

                val resp : KakaoLoginResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(code, resp.result)  // 성공
                    else -> loginView.onLoginFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<KakaoLoginResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("LOGIN / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

}