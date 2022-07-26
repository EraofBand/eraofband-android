package com.example.eraofband.remote.getNewBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetNewBandService {
    private lateinit var getBandView: GetNewBandView

    fun setBandView(getBandView: GetNewBandView) {
        this.getBandView = getBandView
    }

    fun getNewBand() {  // 새로 생성된 밴드 6개 리스트 조회
        val getNewBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getNewBandService?.getNewBand()?.enqueue(object : Callback<GetNewBandResponse> {
            override fun onResponse(call: Call<GetNewBandResponse>, response: Response<GetNewBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETBAND / SUCCESS", response.toString())

                val resp : GetNewBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBandView.onGetSuccess(resp.result)  // 성공
                    else -> getBandView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetNewBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETBAND / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}