package com.example.eraofband.remote.board.getBoard

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBoardService {
    private lateinit var getBoardView: GetBoardView

    fun setBoardView(getBoardView: GetBoardView) {
        this.getBoardView = getBoardView
    }

    fun getBoard(jwt: String, boardIdx: Int) {  // 밴드 정보 조회
        val getBoardService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getBoardService?.getBoard(jwt, boardIdx)?.enqueue(object : Callback<GetBoardResponse> {
            override fun onResponse(call: Call<GetBoardResponse>, response: Response<GetBoardResponse>) {
                // 응답이 왔을 때 처리
                Log.d("BOARD / SUCCESS", response.toString())

                val resp : GetBoardResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBoardView.onGetSuccess(resp.result)  // 성공
                    else -> getBoardView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetBoardResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("BOARD / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}