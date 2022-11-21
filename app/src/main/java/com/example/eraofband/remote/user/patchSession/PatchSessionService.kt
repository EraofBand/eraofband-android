package com.example.eraofband.remote.user.patchSession

import android.util.Log
import com.example.eraofband.data.Session
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchSessionService {
    private lateinit var patchSessionView: PatchSessionView

    fun setPatchSessionView(patchSessionView: PatchSessionView) {
        this.patchSessionView = patchSessionView
    }

    fun patchSession(jwt: String, session: Session) {

        val patchSessionService = NetworkModule().getRetrofit()?.create(API::class.java)
        patchSessionService?.patchSession(jwt, session)
            ?.enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    Log.d("CHECK / SUCCESS", response.toString())

                    val resp: BasicResponse = response.body()!!

                    when (val code = resp.code) {
                        1000 -> patchSessionView.onPatchSessionSuccess(resp.result)
                        else -> patchSessionView.onPatchSessionFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.d("CHECK / FAILURE", t.message.toString())
                }
            })
    }
}