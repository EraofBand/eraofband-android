package com.example.eraofband.remote.band.getLikedBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLikedBandService {
    private lateinit var getBandView: GetLikedBandView

    fun setBandView(getBandView: GetLikedBandView) {
        this.getBandView = getBandView
    }

    fun getLikedBand(jwt: String) {  // 밴드 정보 조회
        val getLikedBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getLikedBandService?.getLikedBand(jwt)?.enqueue(object : Callback<GetLikedBandResponse> {
            override fun onResponse(call: Call<GetLikedBandResponse>, response: Response<GetLikedBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETBAND / SUCCESS", response.toString())

                val resp : GetLikedBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBandView.onGetSuccess(resp.result)  // 성공
                    else -> getBandView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetLikedBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETBAND / FAILURE", t.message.toString())
            }

        })  //  api 호출, enqueue에서 응답 처리
    }

}