package com.example.eraofband.remote.board.postBoard

import android.util.Log
import com.example.eraofband.data.Board
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostBoardService {

    private lateinit var postBoardView: PostBoardView

    fun setBoardView(postBoardView: PostBoardView) {
        this.postBoardView = postBoardView
    }

    fun postBoard(jwt : String, board : Board) {

        val postBoardService = NetworkModule().getRetrofit()?.create(API::class.java)

        postBoardService?.postBoard(jwt, board)?.enqueue(object : Callback<PostBoardResponse> {
            override fun onResponse(call: Call<PostBoardResponse>, response: Response<PostBoardResponse>) {
                // 응답이 왔을 때 처리
                Log.d("POST BOARD / SUCCESS", response.toString())

                val resp : PostBoardResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> postBoardView.onPostSuccess(code, resp.result)  // 성공
                    else -> postBoardView.onPostFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PostBoardResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("POST BOARD / FAILURE", t.message.toString())
            }

        })  // 게시글 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}