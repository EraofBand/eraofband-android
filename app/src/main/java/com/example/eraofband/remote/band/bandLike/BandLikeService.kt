package com.example.eraofband.remote.band.bandLike

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BandLikeService {
    private lateinit var likeView : BandLikeView

    fun setLikeView(likeView: BandLikeView) {
        this.likeView = likeView
    }

    fun like(jwt: String, bandIdx: Int) {  // 좋아요
        val bandLikeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        bandLikeService?.bandLike(jwt, bandIdx)?.enqueue(object : Callback<BandLikeResponse> {
            override fun onResponse(call: Call<BandLikeResponse>, response: Response<BandLikeResponse>) {
                // 응답이 왔을 때 처리
                Log.d("LIKE / SUCCESS", response.toString())

                val resp : BandLikeResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> likeView.onLikeSuccess(resp.result)  // 성공
                    else -> likeView.onLikeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<BandLikeResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("LIKE / FAILURE", t.message.toString())
            }

        })  // api 호출, eunqueue에서 응답 처리
    }

    fun deleteLike(jwt: String, bandIdx : Int) {  // 좋아요 취소
        val bandLikeDeleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        bandLikeDeleteService?.bandLikeDelete(jwt, bandIdx)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETELIKE / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> likeView.onDeleteLikeSuccess(resp.result)  // 성공
                    else -> likeView.onDeleteLikeFailure(code, resp.result)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETELIKE / FAILURE", t.message.toString())
            }

        })  // api 호출, eunqueue에서 응답 처리
    }

}