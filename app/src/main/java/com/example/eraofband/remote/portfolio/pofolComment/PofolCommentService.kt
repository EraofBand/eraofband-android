package com.example.eraofband.remote.portfolio.pofolComment

import android.util.Log
import com.example.eraofband.data.Comment
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PofolCommentService {
    private lateinit var commentView: PofolCommentView
    private val pofolCommentService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setCommentView(commentView: PofolCommentView) {
        this.commentView = commentView
    }

    fun getComment(pofolIdx: Int) {  // 댓글 불러오기
        pofolCommentService?.pofolComment(pofolIdx)?.enqueue(object : Callback<PofolCommentResponse> {
            override fun onResponse(call: Call<PofolCommentResponse>, response: Response<PofolCommentResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : PofolCommentResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> commentView.onCommentSuccess(code, resp.result)  // 성공
                    else -> commentView.onCommentFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PofolCommentResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

    fun writeComment(jwt: String, pofolIdx: Int, comment: Comment) {  // 댓글 달기
        pofolCommentService?.pofolWriteComment(jwt, pofolIdx, comment)?.enqueue(object : Callback<PofolCommentWriteResponse> {
            override fun onResponse(call: Call<PofolCommentWriteResponse>, response: Response<PofolCommentWriteResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : PofolCommentWriteResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> commentView.onCommentWriteSuccess(code, resp.result)  // 성공
                    else -> commentView.onCommentWriteFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PofolCommentWriteResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

    fun deleteComment(jwt: String, pofolCommentIdx: Int, userIdx: Int) {  // 댓글 삭제하기
        pofolCommentService?.pofolDeleteComment(jwt, pofolCommentIdx, userIdx)?.enqueue(object : Callback<PofolCommentDeleteResponse> {
            override fun onResponse(call: Call<PofolCommentDeleteResponse>, response: Response<PofolCommentDeleteResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : PofolCommentDeleteResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> commentView.onCommentDeleteSuccess(code, resp.result)  // 성공
                    else -> commentView.onCommentDeleteFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PofolCommentDeleteResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }

}