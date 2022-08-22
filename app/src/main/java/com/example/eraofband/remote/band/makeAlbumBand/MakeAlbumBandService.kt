package com.example.eraofband.remote.band.makeAlbumBand

import android.util.Log
import com.example.eraofband.data.Album
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeAlbumBandService {
    private lateinit var makeAlbumBandView: MakeAlbumBandView

    fun setAlbumBandView(makeAlbumBandView: MakeAlbumBandView) {
        this.makeAlbumBandView = makeAlbumBandView
    }

    fun makeAlbumBand(jwt: String, album: Album) {  // 밴드 정보 조회
        val makeAlbumBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeAlbumBandService?.makeAlbum(jwt, album)?.enqueue(object : Callback<MakeAlbumBandResponse> {
            override fun onResponse(call: Call<MakeAlbumBandResponse>, response: Response<MakeAlbumBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE ALBUM / SUCCESS", response.toString())

                val resp : MakeAlbumBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeAlbumBandView.onMakeSuccess(resp.result)  // 성공
                    else -> makeAlbumBandView.onMakeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakeAlbumBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("MAKE ALBUM / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}