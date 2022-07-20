package com.example.eraofband.remote.getMyPage

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMyPageService {
    private lateinit var getMyPageView: GetMyPageView

    fun setUserView(getMyPageView: GetMyPageView) {
        this.getMyPageView = getMyPageView
    }

    fun getMyInfo(jwt: String, userIdx: Int) {

        val getMyPageService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getMyPageService?.getMyInfo(jwt, userIdx)?.enqueue(object : Callback<GetMyPageResponse> {
            override fun onResponse(call: Call<GetMyPageResponse>, response: Response<GetMyPageResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET / SUCCESS", response.toString())

                val resp : GetMyPageResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getMyPageView.onGetSuccess(code, resp.result)  // 성공
                    else -> getMyPageView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetMyPageResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}