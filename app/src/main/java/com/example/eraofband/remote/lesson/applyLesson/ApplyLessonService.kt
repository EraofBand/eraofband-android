package com.example.eraofband.remote.lesson.applyLesson

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyLessonService {
    private lateinit var applyView : ApplyLessonView

    fun setApplyView(applyView: ApplyLessonView) {
        this.applyView = applyView
    }

    fun applyLesson(jwt: String, lessonIdx: Int) {  // 밴드 지원하기
        val lessonApplyService =  NetworkModule().getRetrofit()?.create(API::class.java)

        lessonApplyService?.applyLesson(jwt, lessonIdx)?.enqueue(object : Callback<ApplyLessonResponse> {
            override fun onResponse(call: Call<ApplyLessonResponse>, response: Response<ApplyLessonResponse>) {
                // 응답이 왔을 때 처리
                Log.d("APPLY / SUCCESS", response.toString())

                val resp : ApplyLessonResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> applyView.onApplyLessonSuccess(resp.result)
                    else -> applyView.onApplyLessonFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<ApplyLessonResponse>, t: Throwable) {
                Log.d("LIKEAPPLY / FAILURE", t.message.toString())
            }
        })
    }
}