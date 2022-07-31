package com.example.eraofband.remote.user.patchSession

import android.util.Log
import com.example.eraofband.data.Session
import com.example.eraofband.remote.API
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
            ?.enqueue(object : Callback<PatchSessionResponse> {
                override fun onResponse(
                    call: Call<PatchSessionResponse>,
                    response: Response<PatchSessionResponse>
                ) {
                    Log.d("CHECK / SUCCESS", response.toString())

                    val resp: PatchSessionResponse = response.body()!!

                    when (val code = resp.code) {
                        1000 -> patchSessionView.onPatchSessionSuccess(resp.result)
                        else -> patchSessionView.onPatchSessionFailure(code, resp.message)
                    }
                }

                override fun onFailure(call: Call<PatchSessionResponse>, t: Throwable) {
                    Log.d("CHECK / FAILURE", t.message.toString())
                }
            })
    }
}