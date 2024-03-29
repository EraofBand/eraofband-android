package com.example.eraofband.remote.user.patchUser

import android.util.Log
import com.example.eraofband.data.EditUser
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatchUserService {
    private lateinit var patchUserView: PatchUserView

    fun setPatchUserView(patchUserView: PatchUserView) {
        this.patchUserView = patchUserView
    }

    fun patchUser(jwt: String, user: EditUser) {

        val patchUserService =  NetworkModule().getRetrofit()?.create(API::class.java)

        patchUserService?.patchUser(jwt, user)?.enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    // 응답이 왔을 때 처리
                    Log.d("PATCH / SUCCESS", response.toString())

                    val resp: BasicResponse = response.body()!!

                    when (val code = resp.code) {
                        1000 -> patchUserView.onPatchSuccess(code, resp.result)  // 성공
                        else -> patchUserView.onPatchFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    // 네트워크 연결이 실패했을 때 실행
                    Log.d("PATCH / FAILURE", t.message.toString())
                }

            })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}