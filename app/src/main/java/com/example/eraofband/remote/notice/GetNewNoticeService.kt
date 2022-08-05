package com.example.eraofband.remote.notice

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetNewNoticeService {
    private lateinit var getNewNoticeView: GetNewNoticeView

    fun setNewNoticeView(getNewNoticeView: GetNewNoticeView) {
        this.getNewNoticeView = getNewNoticeView
    }

    fun getNewNotice(jwt: String) {

        val getNewNoticeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getNewNoticeService?.getNewNotict(jwt)?.enqueue(object : Callback<GetNewNoticeResponse> {
            override fun onResponse(call: Call<GetNewNoticeResponse>, response: Response<GetNewNoticeResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET NEW NOTICE / SUCCESS", response.toString())

                val resp : GetNewNoticeResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getNewNoticeView.onGetSuccess(resp.result)  // 성공
                    else -> getNewNoticeView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetNewNoticeResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETNOTICE / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}