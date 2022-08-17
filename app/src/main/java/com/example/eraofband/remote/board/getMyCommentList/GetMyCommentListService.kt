package com.example.eraofband.remote.board.getMyCommentList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMyCommentListService {
    private lateinit var getMyCommentListView: GetMyCommentListView

    fun setMyCommentListView(getMyCommentListView: GetMyCommentListView) {
        this.getMyCommentListView = getMyCommentListView
    }

    fun getMyCommentList(jwt: String) {

        val getMyCommentListService = NetworkModule().getRetrofit()?.create(API::class.java)

        getMyCommentListService?.getMyCommentList(jwt)?.enqueue(object : Callback<GetMyCommentListResponse> {
            override fun onResponse(call: Call<GetMyCommentListResponse>, response: Response<GetMyCommentListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET BOARD LIST / SUCCESS", response.toString())

                val resp : GetMyCommentListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getMyCommentListView.onGetCListSuccess(resp.result)  // 성공
                    else -> getMyCommentListView.onGetCListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetMyCommentListResponse>, t: Throwable) {
                Log.d("GET BOARD LIST / FAILURE", t.message.toString())
            }
        })
    }
}