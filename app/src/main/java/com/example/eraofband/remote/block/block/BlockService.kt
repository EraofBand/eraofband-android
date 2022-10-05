package com.example.eraofband.remote.block.block

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockService {
    private lateinit var blockView: BlockView
    private val blockService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setBlockView(blockView: BlockView) {
        this.blockView = blockView
    }

    fun block(jwt: String, userIdx: Int) {  // 유저 차단
        blockService?.block(jwt, userIdx)?.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("BLOCK / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(resp.code) {
                    1000 -> blockView.onBlockSuccess(resp.result)  // 성공
                    else -> blockView.onBlockFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("BLOCK / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}