package com.example.eraofband.remote.search.getUser

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetSearchUserService {
    private lateinit var getUserView: GetSearchUserView

    fun setUserView(getUserView: GetSearchUserView) {
        this.getUserView = getUserView
    }

    fun getSearchUser(keyword : String) {  // 새로 생성된 밴드 6개 리스트 조회
        val getSearchUserService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getSearchUserService?.getSearchUser(keyword)?.enqueue(object : Callback<GetSearchUserResponse> {
            override fun onResponse(call: Call<GetSearchUserResponse>, response: Response<GetSearchUserResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET USER / SUCCESS", response.toString())

                val resp : GetSearchUserResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getUserView.onGetSearchUserSuccess(resp.result)  // 성공
                    else -> getUserView.onGetSearchUserFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetSearchUserResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET USER / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }

}