package com.example.eraofband.remote.notice

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetNoticeService {
    private lateinit var getNoticeView: GetNoticeView

    fun setNoticeView(getNoticeView: GetNoticeView) {
        this.getNoticeView = getNoticeView
    }

    fun getNotice(jwt: String, userIdx: Int) {

        val getNoticeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getNoticeService?.getNotice(jwt, userIdx)?.enqueue(object : Callback<GetNoticeResponse> {
            override fun onResponse(call: Call<GetNoticeResponse>, response: Response<GetNoticeResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETNOTICE / SUCCESS", response.toString())

                val resp : GetNoticeResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getNoticeView.onGetSuccess(resp.result)  // 성공
                    else -> getNoticeView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetNoticeResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETNOTICE / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}