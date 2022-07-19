package com.example.eraofband.remote.patchuser

import android.util.Log
import com.example.eraofband.data.EditUser
import com.example.eraofband.data.User
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

        val patchUserService =  NetworkModule().getRetrofit()?.create(PatchUserInterface::class.java)

        patchUserService?.patchUser(jwt, user)?.enqueue(object : Callback<PatchUserResponse> {
                override fun onResponse(
                    call: Call<PatchUserResponse>,
                    response: Response<PatchUserResponse>
                ) {
                    // 응답이 왔을 때 처리
                    Log.d("PATCH / SUCCESS", response.toString())

                    val resp: PatchUserResponse = response.body()!!

                    when (val code = resp.code) {
                        1000 -> patchUserView.onPatchSuccess(code, resp.result)  // 성공
                        else -> patchUserView.onPatchFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<PatchUserResponse>, t: Throwable) {
                    // 네트워크 연결이 실패했을 때 실행
                    Log.d("PATCH / FAILURE", t.message.toString())
                }

            })  // 유저 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}