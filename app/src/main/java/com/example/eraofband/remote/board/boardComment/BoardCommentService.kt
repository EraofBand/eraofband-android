package com.example.eraofband.remote.board.boardComment

import android.util.Log
import com.example.eraofband.data.Comment
import com.example.eraofband.data.Reply
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardCommentService {
    private lateinit var boardView: BoardCommentView
    private val boardService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setBoardView(boardView: BoardCommentView) {
        this.boardView = boardView
    }

    fun writeComment(jwt: String, boardIdx: Int, comment: Comment) {  // 댓글 달기
        boardService?.writeBoardComment(jwt, boardIdx, comment)?.enqueue(object : Callback<BoardWriteCommentResponse> {
            override fun onResponse(call: Call<BoardWriteCommentResponse>, response: Response<BoardWriteCommentResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : BoardWriteCommentResponse = response.body()!!

                when(resp.code) {
                    1000 -> boardView.onWriteCommentSuccess(resp.result)  // 성공
                    else -> boardView.onWriteCommentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BoardWriteCommentResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }

    fun deleteComment(jwt: String, boardCommentIdx: Int, userIdx: Int) {  // 댓글, 답글 삭제 조회
        boardService?.deleteBoardComment(jwt, boardCommentIdx, userIdx)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(resp.code) {
                    1000 -> boardView.onDeleteCommentSuccess(resp.result)  // 성공
                    else -> boardView.onDeleteCommentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }

    fun writeReply(jwt: String, boardIdx: Int, reply: Reply) {  // 댓글 달기
        boardService?.writeBoardReply(jwt, boardIdx, reply)?.enqueue(object : Callback<BoardWriteCommentResponse> {
            override fun onResponse(call: Call<BoardWriteCommentResponse>, response: Response<BoardWriteCommentResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : BoardWriteCommentResponse = response.body()!!

                when(resp.code) {
                    1000 -> boardView.onWriteCommentSuccess(resp.result)  // 성공
                    else -> boardView.onWriteCommentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BoardWriteCommentResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}