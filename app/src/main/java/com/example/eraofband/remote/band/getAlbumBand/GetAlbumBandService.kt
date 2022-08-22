package com.example.eraofband.remote.band.getAlbumAlbumBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandResponse
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAlbumBandService {
    private lateinit var getAlbumBandView: GetAlbumBandView

    fun setAlbumBandView(getAlbumBandView: GetAlbumBandView) {
        this.getAlbumBandView = getAlbumBandView
    }

    fun getAlbumBand(jwt: String, bandIdx: Int) {  // 밴드 정보 조회
        val getAlbumBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getAlbumBandService?.getAlbumBand(jwt, bandIdx)?.enqueue(object : Callback<GetAlbumBandResponse> {
            override fun onResponse(call: Call<GetAlbumBandResponse>, response: Response<GetAlbumBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GETALBUM / SUCCESS", response.toString())

                val resp : GetAlbumBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getAlbumBandView.onGetSuccess(resp.result)  // 성공
                    else -> getAlbumBandView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetAlbumBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GETALBUM / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}