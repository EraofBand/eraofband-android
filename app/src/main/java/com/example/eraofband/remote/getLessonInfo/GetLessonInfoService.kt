package com.example.eraofband.remote.getLessonInfo

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLessonInfoService {
    private lateinit var makeView : GetLessonInfoView

    fun getLessonInfoView(makeView: GetLessonInfoView) {
        this.makeView = makeView
    }

    fun getLessonInfo(jwt: String, lessonIdx: Int) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.getLessonInfo(jwt, lessonIdx)?.enqueue(object : Callback<GetLessonInfoResponse> {
            override fun onResponse(call: Call<GetLessonInfoResponse>, response: Response<GetLessonInfoResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : GetLessonInfoResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onGetLessonInfoSuccess(code, resp.result)  // 성공
                    else -> makeView.onGetLessonInfoFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetLessonInfoResponse>, t: Throwable) {
                Log.d("MAKE / FAILURE", t.message.toString())
            }
        })
    }
}