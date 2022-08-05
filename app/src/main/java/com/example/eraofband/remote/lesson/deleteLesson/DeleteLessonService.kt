package com.example.eraofband.remote.lesson.deleteLesson

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteLessonService {
    private lateinit var deleteView : DeleteLessonView

    fun setDeleteView(deleteView: DeleteLessonView) {
        this.deleteView = deleteView
    }

    fun deleteLesson(jwt: String, lessonIdx: Int, userIdx: Int) {

        val deleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        deleteService?.deleteLesson(jwt, lessonIdx, userIdx)?.enqueue(object : Callback<DeleteLessonResponse> {
            override fun onResponse(call: Call<DeleteLessonResponse>, response: Response<DeleteLessonResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETE / SUCCESS", response.toString())

                val resp : DeleteLessonResponse = response.body()!!

                when(resp.code) {
                    1000 -> deleteView.onDeleteSuccess(resp.result)  // 성공
                    else -> deleteView.onDeleteFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<DeleteLessonResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETE / FAILURE", t.message.toString())
            }

        })  //레슨 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}