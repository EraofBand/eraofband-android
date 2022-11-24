package com.example.eraofband.remote.portfolio.pofolLike

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PofolLikeService {
    private lateinit var likeView : PofolLikeView

    fun setLikeView(likeView: PofolLikeView) {
        this.likeView = likeView
    }

    fun like(jwt: String, pofolIdx: Int) {  // 좋아요

        val pofolLikeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        pofolLikeService?.pofolLike(jwt, pofolIdx)?.enqueue(object : Callback<PofolLikeResponse> {
            override fun onResponse(call: Call<PofolLikeResponse>, response: Response<PofolLikeResponse>) {
                // 응답이 왔을 때 처리
                Log.d("LIKE / SUCCESS", response.toString())

                val resp : PofolLikeResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> likeView.onLikeSuccess(code, resp.result)  // 성공
                    else -> likeView.onLikeFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PofolLikeResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("LIKE / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

    fun deleteLike(jwt: String, pofolIdx : Int) {  // 좋아요 취소

        val pofolLikeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        pofolLikeService?.pofolDeleteLike(jwt, pofolIdx)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETELIKE / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> likeView.onDeleteLikeSuccess(code, resp.result)  // 성공
                    else -> likeView.onDeleteLikeFailure(code, resp.result)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETELIKE / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

}