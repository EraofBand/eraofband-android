package com.example.eraofband.remote.band.applyBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyBandService {
    private lateinit var applyView : ApplyBandView

    fun setApplyView(applyView: ApplyBandView) {
        this.applyView = applyView
    }

    fun applyBand(jwt: String, bandIdx: Int, session: Int) {  // 밴드 지원하기
        val bandApplyService =  NetworkModule().getRetrofit()?.create(API::class.java)

        bandApplyService?.applyBand(jwt, bandIdx, session)?.enqueue(object : Callback<ApplyBandResponse> {
            override fun onResponse(call: Call<ApplyBandResponse>, response: Response<ApplyBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("APPLY / SUCCESS", response.toString())

                val resp : ApplyBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> applyView.onApplySuccess(resp.result)  // 성공
                    else -> applyView.onApplyFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<ApplyBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("APPLY / FAILURE", t.message.toString())
            }

        })  // api 호출, eunqueue에서 응답 처리
    }
}