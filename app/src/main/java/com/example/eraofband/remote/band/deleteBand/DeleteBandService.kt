package com.example.eraofband.remote.band.deleteBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteBandService {
    private lateinit var deleteView : DeleteBandView

    fun setDeleteView(deleteView: DeleteBandView) {
        this.deleteView = deleteView
    }

    fun deleteBand(jwt: String, bandIdx: Int, userIdx : Int) {

        val deleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        deleteService?.deleteBand(jwt, bandIdx, userIdx)?.enqueue(object : Callback<DeleteBandResponse> {
            override fun onResponse(call: Call<DeleteBandResponse>, response: Response<DeleteBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETE / SUCCESS", response.toString())

                val resp : DeleteBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> deleteView.onDeleteSuccess(code, resp.result)  // 성공
                    else -> deleteView.onDeleteFailure(resp)
                }
            }

            override fun onFailure(call: Call<DeleteBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETE / FAILURE", t.message.toString())
            }

        })  //밴드 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}