package com.example.eraofband.remote.chat.patchChat

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchChatService {
    private lateinit var patchChatView: PatchChatView

    fun setChatView(patchChatView: PatchChatView) {
        this.patchChatView = patchChatView
    }

    fun patchChat(jwt: String, chatRoomIdx: String) {

        val patchChatService = NetworkModule().getRetrofit()?.create(API::class.java)

        patchChatService?.patchChat(jwt, chatRoomIdx)?.enqueue(object : Callback<PatchChatResponse> {
            override fun onResponse(call: Call<PatchChatResponse>, response: Response<PatchChatResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH CHAT / SUCCESS", response.toString())

                val resp : PatchChatResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> patchChatView.onPatchSuccess(resp.result)  // 성공
                    else -> patchChatView.onPatchFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PatchChatResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH CHAT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}