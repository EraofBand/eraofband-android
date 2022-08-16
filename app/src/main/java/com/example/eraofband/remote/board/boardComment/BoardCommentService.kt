package com.example.eraofband.remote.board.boardComment

import android.util.Log
import com.example.eraofband.data.Comment
import com.example.eraofband.remote.API
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

                when(val code = resp.code) {
                    1000 -> boardView.onWriteSuccess(resp.result)  // 성공
                    else -> boardView.onWriteFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<BoardWriteCommentResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }

    fun deleteComment(jwt: String, boardCommentIdx: Int, userIdx: Int) {  // 댓글, 답글 삭제 조회
        boardService?.deleteBoardComment(jwt, boardCommentIdx, userIdx)?.enqueue(object : Callback<BoardDeleteCommentResponse> {
            override fun onResponse(call: Call<BoardDeleteCommentResponse>, response: Response<BoardDeleteCommentResponse>) {
                // 응답이 왔을 때 처리
                Log.d("COMMENT / SUCCESS", response.toString())

                val resp : BoardDeleteCommentResponse = response.body()!!

                when(resp.code) {
                    1000 -> boardView.onDeleteSuccess(resp.result)  // 성공
                    2107 -> boardView.onHaveReply(resp.code, resp.message)
                    else -> boardView.onDeleteFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BoardDeleteCommentResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("COMMENT / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}