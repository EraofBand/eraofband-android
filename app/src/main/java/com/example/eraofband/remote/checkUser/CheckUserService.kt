package com.example.eraofband.remote.checkUser

import android.util.Log
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.getuser.GetUserInterface
import com.example.eraofband.remote.getuser.GetUserResponse
import com.example.eraofband.remote.getuser.GetUserView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckUserService {
    private lateinit var checkUserView: CheckUserView

    fun setUserView(checkUserView: CheckUserView) {
        this.checkUserView = checkUserView
    }

    fun checkUser(email: String) {

        val checkUserService =  NetworkModule().getRetrofit()?.create(CheckUserInterface::class.java)

        checkUserService?.checkUser(email)?.enqueue(object : Callback<CheckUserResponse> {
            override fun onResponse(call: Call<CheckUserResponse>, response: Response<CheckUserResponse>) {
                // 응답이 왔을 때 처리
                Log.d("CHECK / SUCCESS", response.toString())

                val resp : CheckUserResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> checkUserView.onCheckSuccess(resp.message, resp.result)  // 성공
                    else -> checkUserView.onCheckFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<CheckUserResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("CHECK / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}