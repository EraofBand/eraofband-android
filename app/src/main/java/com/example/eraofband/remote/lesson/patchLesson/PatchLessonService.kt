package com.example.eraofband.remote.lesson.patchLesson

import android.util.Log
import com.example.eraofband.data.Lesson
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchLessonService {
    private lateinit var makeView : PatchLessonView

    fun setPatchLessonView(makeView: PatchLessonView) {
        this.makeView = makeView
    }

    fun patchLesson(jwt: String, lessonIdx: Int, lesson: Lesson) {

        val makeService = NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.patchLesson(jwt, lessonIdx, lesson)?.enqueue(object : Callback<PatchLessonResponse> {
            override fun onResponse(call: Call<PatchLessonResponse>, response: Response<PatchLessonResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : PatchLessonResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onPatchLessonSuccess(code, resp.result)  // 성공
                    else -> makeView.onPatchLessonFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<PatchLessonResponse>, t: Throwable) {
                Log.d("MAKE / FAILURE", t.message.toString())
            }
        })
    }
}