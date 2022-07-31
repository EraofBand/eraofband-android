package com.example.eraofband.remote.lesson.getLessonList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLessonListService {
    private lateinit var makeView : GetLessonListView

    fun getLessonListView(makeView: GetLessonListView) {
        this.makeView = makeView
    }

    fun getLessonList(region: String, session : Int) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.getLessonList(region, session)?.enqueue(object : Callback<GetLessonListResponse> {
            override fun onResponse(call: Call<GetLessonListResponse>, response: Response<GetLessonListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : GetLessonListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onGetLessonListSuccess(code, resp.result)  // 성공
                    else -> makeView.onGetLessonListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetLessonListResponse>, t: Throwable) {
                Log.d("MAKE / FAILURE", t.message.toString())
            }
        })
    }
}