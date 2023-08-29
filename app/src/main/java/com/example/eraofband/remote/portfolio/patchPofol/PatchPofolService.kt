package com.example.eraofband.remote.portfolio.patchPofol

import android.util.Log
import com.example.eraofband.data.Portfolio
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
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

        patchService?.patchPofol(jwt, pofolIdx, portfolio)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> patchView.onPatchUserSuccess(code, resp.result)  // 성공
                    else -> patchView.onPatchUserFailure(resp)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH / FAILURE", t.message.toString())
            }

        })  // 포트폴리오 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}