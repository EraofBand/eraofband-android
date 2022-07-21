package com.example.eraofband.remote.patchPofol

import android.util.Log
import com.example.eraofband.data.Portfolio
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchPofolService {
    private lateinit var patchView : PatchPofolView

    fun setPatchView(patchView: PatchPofolView) {
        this.patchView = patchView
    }

    fun patchPortfolio(jwt: String, pofolIdx: Int, portfolio : Portfolio) {

        val patchService =  NetworkModule().getRetrofit()?.create(API::class.java)

        patchService?.patchPofol(jwt, pofolIdx, portfolio)?.enqueue(object : Callback<PatchPofolResponse> {
            override fun onResponse(call: Call<PatchPofolResponse>, response: Response<PatchPofolResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH / SUCCESS", response.toString())

                val resp : PatchPofolResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> patchView.onPatchSuccess(code, resp.result)  // 성공
                    else -> patchView.onPatchFailure(resp)
                }
            }

            override fun onFailure(call: Call<PatchPofolResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH / FAILURE", t.message.toString())
            }

        })  // 포트폴리오 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}