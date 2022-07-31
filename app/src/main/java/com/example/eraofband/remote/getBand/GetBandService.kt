package com.example.eraofband.remote.getBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBandService {
    private lateinit var getBandView: GetBandView

    fun setBandView(getBandView: GetBandView) {
        this.getBandView = getBandView
    }

    fun getBand(jwt: String, bandIdx: Int) {  // 밴드 정보 조회
        val getBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getBandService?.getBand(jwt, bandIdx)?.enqueue(object : Callback<GetBandResponse> {
            override fun onResponse(call: Call<GetBandResponse>, response: Response<GetBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETBAND / SUCCESS", response.toString())

                val resp : GetBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBandView.onGetSuccess(resp.result)  // 성공
                    else -> getBandView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETBAND / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}