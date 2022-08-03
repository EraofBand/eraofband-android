package com.example.eraofband.remote.getRegionBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetRegionBandService {
    private lateinit var getView : GetRegionBandView

    fun setGetView(getView: GetRegionBandView) {
        this.getView = getView
    }

    fun getRegionBand(bandRegion: String, bandSession: Int) {

        val getService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getService?.getRegionBand(bandRegion, bandSession)?.enqueue(object : Callback<GetRegionBandResponse> {
            override fun onResponse(call: Call<GetRegionBandResponse>, response: Response<GetRegionBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET / SUCCESS", response.toString())

                val resp : GetRegionBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getView.onGetSuccess(code, resp.result)  // 성공
                    else -> getView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetRegionBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET / FAILURE", t.message.toString())
            }

        })  // 밴드 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}