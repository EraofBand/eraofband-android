package com.example.eraofband.remote.board.getMyMyBoardList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResponse
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMyBoardListService {
    private lateinit var getMyBoardListView: GetMyBoardListView

    fun setMyBoardListView(getMyBoardListView: GetMyBoardListView) {
        this.getMyBoardListView = getMyBoardListView
    }

    fun getMyBoardList(jwt: String) {

        val getMyBoardListService = NetworkModule().getRetrofit()?.create(API::class.java)

        getMyBoardListService?.getMyBoardList(jwt)?.enqueue(object : Callback<GetMyBoardListResponse> {
            override fun onResponse(call: Call<GetMyBoardListResponse>, response: Response<GetMyBoardListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET BOARD LIST / SUCCESS", response.toString())

                val resp : GetMyBoardListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getMyBoardListView.onGetListSuccess(resp.result)  // 성공
                    else -> getMyBoardListView.onGetListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetMyBoardListResponse>, t: Throwable) {
                Log.d("GET BOARD LIST / FAILURE", t.message.toString())
            }
        })
    }
}