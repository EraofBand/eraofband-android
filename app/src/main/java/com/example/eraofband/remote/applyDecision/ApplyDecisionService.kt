package com.example.eraofband.remote.applyDecision

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyDecisionService {
    private lateinit var decisionView : ApplyDecisionView
    private val decisionService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setDecisionView(decisionView: ApplyDecisionView) {
        this.decisionView = decisionView
    }

    fun acceptApply(bandIdx: Int, userIdx: Int) {  // 밴드 지원 수락하기
        decisionService?.acceptApply(bandIdx, userIdx)?.enqueue(object : Callback<AcceptApplyResponse> {
            override fun onResponse(call: Call<AcceptApplyResponse>, response: Response<AcceptApplyResponse>) {
                // 응답이 왔을 때 처리
                Log.d("ACCEPT / SUCCESS", response.toString())

                val resp : AcceptApplyResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> decisionView.onAcceptSuccess(resp.result)  // 성공
                    else -> decisionView.onAcceptFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<AcceptApplyResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("ACCEPT / FAILURE", t.message.toString())
            }

        })  // api 호출, eunqueue에서 응답 처리
    }

    fun rejectApply(bandIdx: Int, userIdx: Int) {  // 밴드 지원 수락하기
        decisionService?.rejectApply(bandIdx, userIdx)?.enqueue(object : Callback<RejectApplyResponse> {
            override fun onResponse(call: Call<RejectApplyResponse>, response: Response<RejectApplyResponse>) {
                // 응답이 왔을 때 처리
                Log.d("REJECT / SUCCESS", response.toString())

                val resp : RejectApplyResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> decisionView.onRejectSuccess(resp.result)  // 성공
                    else -> decisionView.onRejectFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<RejectApplyResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("REJECT / FAILURE", t.message.toString())
            }

        })  // api 호출, eunqueue에서 응답 처리
    }
}