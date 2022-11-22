package com.example.eraofband.remote.chat.enterChatRoom

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterChatRoomService {
    private lateinit var enterChatRoomView: EnterChatRoomView

    fun setEnterChatRoomView(enterChatRoomView: EnterChatRoomView) {
        this.enterChatRoomView = enterChatRoomView
    }

    fun enterChatRoom(jwt: String, chatRoomIdx: String) {

        val enterChatRoomService =  NetworkModule().getRetrofit()?.create(API::class.java)

        enterChatRoomService?.enterChatRoom(jwt,chatRoomIdx)?.enqueue(object : Callback<EnterChatRoomResponse> {
            override fun onResponse(call: Call<EnterChatRoomResponse>, response: Response<EnterChatRoomResponse>) {
                // 응답이 왔을 때 처리
                Log.d("ENTER CHATROOM / SUCCESS", response.toString())

                val resp : EnterChatRoomResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> enterChatRoomView.onEnterSuccess(resp.result)  // 성공
                    else -> enterChatRoomView.onEnterFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<EnterChatRoomResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("ENTER CHATROOM / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}