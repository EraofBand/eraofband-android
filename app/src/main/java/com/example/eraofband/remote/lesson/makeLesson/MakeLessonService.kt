package com.example.eraofband.remote.lesson.makeLesson

import android.util.Log
import com.example.eraofband.data.Lesson
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeLessonService {
    private lateinit var makeView : MakeLessonView

    fun setMakeLessonView(makeView: MakeLessonView) {
        this.makeView = makeView
    }

    fun makeLesson(jwt: String, lesson : Lesson) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.makeLesson(jwt, lesson)?.enqueue(object : Callback<MakeLessonResponse> {
            override fun onResponse(call: Call<MakeLessonResponse>, response: Response<MakeLessonResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : MakeLessonResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onMakeLessonSuccess(code, resp.result)  // 성공
                    else -> makeView.onMakeLessonFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakeLessonResponse>, t: Throwable) {
                Log.d("MAKE / FAILURE", t.message.toString())
            }
        })
    }
}