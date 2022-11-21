package com.example.eraofband.remote.lesson.deleteUserLesson

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteUserLessonService {private lateinit var deleteView : DeleteUserLessonView

    fun setDeleteView(deleteView: DeleteUserLessonView) {
        this.deleteView = deleteView
    }

    fun deleteUserLesson(jwt: String, lessonIdx: Int) {

        val deleteService =  NetworkModule().getRetrofit()?.create(API::class.java)

        deleteService?.deleteUserLesson(jwt, lessonIdx)?.enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("DELETE / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(resp.code) {
                    1000 -> deleteView.onDeleteUserSuccess(resp.result)  // 성공
                    else -> deleteView.onDeleteUserFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("DELETE / FAILURE", t.message.toString())
            }

        })  //레슨 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}