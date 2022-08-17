package com.example.eraofband.remote.board.boardLike

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardLikeService {
    private lateinit var likeView: BoardLikeView
    private val getBoardService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setLikeView(likeView: BoardLikeView) {
        this.likeView = likeView
    }

    fun like(jwt: String, boardIdx: Int) {  // 밴드 정보 조회
        getBoardService?.likeBoard(jwt, boardIdx)?.enqueue(object : Callback<BoardLikeResponse> {
            override fun onResponse(call: Call<BoardLikeResponse>, response: Response<BoardLikeResponse>) {
                // 응답이 왔을 때 처리
                Log.d("BOARD / SUCCESS", response.toString())

                val resp : BoardLikeResponse = response.body()!!

                when(resp.code) {
                    1000 -> likeView.onLikeSuccess(resp.result)  // 성공
                    else -> likeView.onLikeFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BoardLikeResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("BOARD / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}