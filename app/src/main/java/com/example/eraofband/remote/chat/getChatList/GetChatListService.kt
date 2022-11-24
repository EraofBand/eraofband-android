package com.example.eraofband.remote.chat.getChatList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetChatListService {
    private lateinit var getChatListView: GetChatListView

    fun setChatListView(getChatListView: GetChatListView) {
        this.getChatListView = getChatListView
    }

    fun getChatList(jwt: String) {

        val getChatListService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getChatListService?.getChatList(jwt)?.enqueue(object : Callback<GetChatListResponse> {
            override fun onResponse(call: Call<GetChatListResponse>, response: Response<GetChatListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET CHAT LIST / SUCCESS", response.toString())

                val resp : GetChatListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getChatListView.onGetListSuccess(resp.result)  // 성공
                    else -> getChatListView.onGetListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetChatListResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET CHAT LIST / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}