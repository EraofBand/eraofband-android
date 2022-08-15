package com.example.eraofband.remote.board.getBoardList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBoardListService {
    private lateinit var getBoardListView: GetBoardListView

    fun setBoardListView(getBoardListView: GetBoardListView) {
        this.getBoardListView = getBoardListView
    }

    fun getBoardList(category: Int) {

        val getBoardListService = NetworkModule().getRetrofit()?.create(API::class.java)

        getBoardListService?.getBoardList(category)?.enqueue(object : Callback<GetBoardListResponse> {
            override fun onResponse(call: Call<GetBoardListResponse>, response: Response<GetBoardListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET BOARD LIST / SUCCESS", response.toString())

                val resp : GetBoardListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBoardListView.onGetListSuccess(resp.result)  // 성공
                    else -> getBoardListView.onGetListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetBoardListResponse>, t: Throwable) {
                Log.d("GET Board LIST / FAILURE", t.message.toString())
            }
        })
    }
}