package com.example.eraofband.remote.board.deleteBoard

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteBoardService {
    private lateinit var deleteView: DeleteBoardView

    fun setDeleteView(deleteView: DeleteBoardView) {
        this.deleteView = deleteView
    }

    fun deleteBoard(jwt: String, boardIdx: Int, userIdx: Int) {  // 밴드 정보 조회
        val getBoardService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getBoardService?.deleteBoard(jwt, boardIdx, userIdx)?.enqueue(object : Callback<DeleteBoardResponse> {
            override fun onResponse(call: Call<DeleteBoardResponse>, response: Response<DeleteBoardResponse>) {
                // 응답이 왔을 때 처리
                Log.d("BOARD / SUCCESS", response.toString())

                val resp : DeleteBoardResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> deleteView.onDeleteSuccess(resp.result)  // 성공
                    else -> deleteView.onDeleteFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<DeleteBoardResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("BOARD / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}