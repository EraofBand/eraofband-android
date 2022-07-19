package com.example.eraofband.remote.sendimg

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendImgService {
    private lateinit var imageView: SendImgView

    fun setImageView(imageView: SendImgView) {
        this.imageView = imageView
    }

    fun sendImg(url: MultipartBody.Part) {

        val sendImgService =  NetworkModule().getRetrofit()?.create(API::class.java)

        sendImgService?.sendImg(url)?.enqueue(object : Callback<SendImgResponse> {
            override fun onResponse(call: Call<SendImgResponse>, response: Response<SendImgResponse>) {
                // 응답이 왔을 때 처리
                Log.d("SEND / SUCCESS", response.toString())

                val resp : SendImgResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> imageView.onSendSuccess(resp)  // 성공
                    else -> imageView.onSendFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<SendImgResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("SEND / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }
}