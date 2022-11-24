package com.example.eraofband.remote.band.patchBand

import android.util.Log
import com.example.eraofband.data.Band
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchBandService {
    private lateinit var patchView : PatchBandView

    fun setPatchView(patchView: PatchBandView) {
        this.patchView = patchView
    }

    fun patchBand(jwt: String, bandIdx: Int, band : Band) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.patchBand(jwt, bandIdx, band)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> patchView.onPatchSuccess(code, resp.result)  // 성공
                    else -> patchView.onPatchFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH / FAILURE", t.message.toString())
            }

        })  // 밴드 수정된 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}