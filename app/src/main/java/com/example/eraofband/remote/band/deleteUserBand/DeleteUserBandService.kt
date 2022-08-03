package com.example.eraofband.remote.band.deleteUserBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteUserBandService {
    private lateinit var deleteView : DeleteUserBandView

    fun setDeleteView(deleteView: DeleteUserBandView) {
        this.deleteView = deleteView
    }

    fun deleteUserBand(jwt: String, bandIdx: Int) {

        val deleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        deleteService?.deleteUserBand(jwt, bandIdx)?.enqueue(object :
            Callback<DeleteUserBandResponse> {
            override fun onResponse(call: Call<DeleteUserBandResponse>, response: Response<DeleteUserBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETE / SUCCESS", response.toString())

                val resp : DeleteUserBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> deleteView.onDeleteUserSuccess(code, resp.result)  // 성공
                    else -> deleteView.onDeleteUserFailure(resp)
                }
            }

            override fun onFailure(call: Call<DeleteUserBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETE / FAILURE", t.message.toString())
            }

        })  //밴드 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}