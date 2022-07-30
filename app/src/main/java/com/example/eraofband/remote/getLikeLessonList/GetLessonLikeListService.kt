package com.example.eraofband.remote.getLessonLikeList

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.getLikeLessonList.GetLessonLikeListResponse
import com.example.eraofband.remote.getLikeLessonList.GetLessonLikeListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLessonLikeListService {
    private lateinit var makeView : GetLessonLikeListView

    fun getLessonLikeListView(makeView: GetLessonLikeListView) {
        this.makeView = makeView
    }

    fun getLessonLikeList(jwt: String) {

        val makeService =  NetworkModule().getRetrofit()?.create(API::class.java)

        makeService?.getLessonLikeList(jwt)?.enqueue(object : Callback<GetLessonLikeListResponse> {
            override fun onResponse(call: Call<GetLessonLikeListResponse>, response: Response<GetLessonLikeListResponse>) {
                // 응답이 왔을 때 처리
                Log.d("MAKE / SUCCESS", response.toString())

                val resp : GetLessonLikeListResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> makeView.onGetLessonLikeListSuccess(code, resp.result)  // 성공
                    else -> makeView.onGetLessonLikeListFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetLessonLikeListResponse>, t: Throwable) {
                Log.d("MAKE / FAILURE", t.message.toString())
            }
        })
    }
}