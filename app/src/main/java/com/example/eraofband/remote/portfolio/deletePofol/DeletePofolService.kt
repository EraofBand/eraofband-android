package com.example.eraofband.remote.portfolio.deletePofol

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeletePofolService {
    private lateinit var deleteView : DeletePofolView

    fun setDeleteView(deleteView: DeletePofolView) {
        this.deleteView = deleteView
    }

    fun deletePortfolio(jwt: String, pofolIdx: Int, userIdx : Int) {

        val deleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        deleteService?.deletePofol(jwt, pofolIdx, userIdx)?.enqueue(object : Callback<DeletePofolResponse> {
            override fun onResponse(call: Call<DeletePofolResponse>, response: Response<DeletePofolResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETE / SUCCESS", response.toString())

                val resp : DeletePofolResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> deleteView.onDeleteSuccess(code, resp.result)  // 성공
                    else -> deleteView.onDeleteFailure(resp)
                }
            }

            override fun onFailure(call: Call<DeletePofolResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETE / FAILURE", t.message.toString())
            }

        })  // 포트폴리오 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}