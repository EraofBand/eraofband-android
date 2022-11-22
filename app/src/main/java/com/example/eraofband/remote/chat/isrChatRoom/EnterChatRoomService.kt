package com.example.eraofband.remote.chat.isChatRoom

import android.util.Log
import com.example.eraofband.data.Users
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IsChatRoomService {
    private lateinit var chatRoomView: IsChatRoomView

    fun setChatListView(chatRoomView: IsChatRoomView) {
        this.chatRoomView = chatRoomView
    }

    fun isChatRoom(jwt: String, user: Users) {
        val chatRoomService =  NetworkModule().getRetrofit()?.create(API::class.java)

        chatRoomService?.isChatRoom(jwt, user)?.enqueue(object : Callback<IsChatRoomResponse> {
            override fun onResponse(call: Call<IsChatRoomResponse>, response: Response<IsChatRoomResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET / SUCCESS", response.toString())

                val resp : IsChatRoomResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> chatRoomView.onGetSuccess(resp.result)  // 성공
                    else -> chatRoomView.onGetFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<IsChatRoomResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}