package com.example.eraofband.remote.block.getBlock

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBlockService {
    private lateinit var blockView: GetBlockView
    private val blockService =  NetworkModule().getRetrofit()?.create(API::class.java)

    fun setBlockView(blockView: GetBlockView) {
        this.blockView = blockView
    }

    fun getBlockList(jwt: String) {  // 차단 리스트
        blockService?.getBlock(jwt)?.enqueue(object : Callback<GetBlockResponse> {
            override fun onResponse(call: Call<GetBlockResponse>, response: Response<GetBlockResponse>) {
                // 응답이 왔을 때 처리
                Log.d("BLOCK / SUCCESS", response.toString())

                val resp : GetBlockResponse = response.body()!!

                when(resp.code) {
                    1000 -> blockView.onBlockSuccess(resp.result)  // 성공
                    else -> blockView.onBlockFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetBlockResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("BLOCK / FAILURE", t.message.toString())
            }

        })  // api 호출, enqueue에서 응답 처리
    }
}