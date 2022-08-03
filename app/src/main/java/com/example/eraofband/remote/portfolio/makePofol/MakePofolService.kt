package com.example.eraofband.remote.portfolio.makePofol

import android.util.Log
import com.example.eraofband.data.Portfolio
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakePofolService {
    private lateinit var makeView : MakePofolView

    fun setMakeView(makeView: MakePofolView) {
        this.makeView = makeView
    }

    fun makePortfolio(jwt: String, portfolio : Portfolio) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.makePofol(jwt, portfolio)?.enqueue(object : Callback<MakePofolResponse> {
            override fun onResponse(call: Call<MakePofolResponse>, response: Response<MakePofolResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : MakePofolResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onMakeSuccess(code, resp.result)  // 성공
                    else -> makeView.onMakeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakePofolResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("MAKE / FAILURE", t.message.toString())
            }

        })  // 포트폴리오 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}