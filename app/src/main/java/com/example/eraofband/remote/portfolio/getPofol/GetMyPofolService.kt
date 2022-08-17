package com.example.eraofband.remote.portfolio.getPofol

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMyPofolService {
    private lateinit var getMyPofolView: GetMyPofolView

    fun setPofolView(getMyPofolView: GetMyPofolView) {
        this.getMyPofolView = getMyPofolView
    }

    fun getPortfolio(jwt: String, userIdx: Int) {

        val getMyPofolService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getMyPofolService?.getMyPofol(jwt, userIdx)?.enqueue(object : Callback<GetPofolResponse> {
            override fun onResponse(call: Call<GetPofolResponse>, response: Response<GetPofolResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETPOFOL / SUCCESS", response.toString())

                val resp : GetPofolResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getMyPofolView.onGetSuccess(resp.result)  // 성공
                    else -> getMyPofolView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetPofolResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETPOFOL / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}