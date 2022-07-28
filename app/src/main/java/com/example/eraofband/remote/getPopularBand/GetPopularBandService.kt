package com.example.eraofband.remote.getPopularBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetPopularBandService {
    private lateinit var getBandView: GetPopularBandView

    fun setBandView(getBandView: GetPopularBandView) {
        this.getBandView = getBandView
    }

    fun getPopularBand() {  // Top 3 밴드 리스트
        val getPopularBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getPopularBandService?.getPopularBand()?.enqueue(object : Callback<GetPopularBandResponse> {
            override fun onResponse(call: Call<GetPopularBandResponse>, response: Response<GetPopularBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETBAND / SUCCESS", response.toString())

                val resp : GetPopularBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBandView.onGetPopSuccess(resp.result)  // 성공
                    else -> getBandView.onGetPopFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetPopularBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETBAND / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }

}