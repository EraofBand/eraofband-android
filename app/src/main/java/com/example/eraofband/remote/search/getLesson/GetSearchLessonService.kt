package com.example.eraofband.remote.search.getLesson

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetSearchLessonService {
    private lateinit var getLessonView: GetSearchLessonView

    fun setLessonView(getLessonView: GetSearchLessonView) {
        this.getLessonView = getLessonView
    }

    fun getSearchLesson(keyword : String) {  // 새로 생성된 밴드 6개 리스트 조회
        val getSearchLessonService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getSearchLessonService?.getSearchLesson(keyword)?.enqueue(object :
            Callback<GetSearchLessonResponse> {
            override fun onResponse(call: Call<GetSearchLessonResponse>, response: Response<GetSearchLessonResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET LESSON / SUCCESS", response.toString())

                val resp : GetSearchLessonResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getLessonView.onGetSearchLessonSuccess(resp.result)  // 성공
                    else -> getLessonView.onGetSearchLessonFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetSearchLessonResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET LESSON / FAILURE", t.message.toString())
            }

        })  // 레슨 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}