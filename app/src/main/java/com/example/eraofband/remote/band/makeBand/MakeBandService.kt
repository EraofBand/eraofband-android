package com.example.eraofband.remote.band.makeBand

import android.util.Log
import com.example.eraofband.data.Band
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeBandService {
    private lateinit var makeView : MakeBandView

    fun setMakeView(makeView: MakeBandView) {
        this.makeView = makeView
    }

    fun makeBand(jwt: String, band : Band) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.makeBand(jwt, band)?.enqueue(object : Callback<MakeBandResponse> {
            override fun onResponse(call: Call<MakeBandResponse>, response: Response<MakeBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : MakeBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onMakeSuccess(code, resp.result)  // 성공
                    else -> makeView.onMakeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakeBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("MAKE / FAILURE", t.message.toString())
            }

        })  // 밴드 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}