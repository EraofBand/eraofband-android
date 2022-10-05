package com.example.eraofband.remote.board.patchBoard

import android.util.Log
import com.example.eraofband.data.BoardContent
import com.example.eraofband.remote.API
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatchBoardService {
    private lateinit var patchBoardView : PatchBoardView

    fun setBoardView(patchBoardView: PatchBoardView) {
        this.patchBoardView = patchBoardView
    }

    fun patchBoard(jwt : String, bandIdx : Int, boardContent : BoardContent) {

        val patchBoardService = NetworkModule().getRetrofit()?.create(API::class.java)

        patchBoardService?.patchBoard(jwt, bandIdx, boardContent)?.enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                // 응답이 왔을 때 처리
                Log.d("PATCH BOARD / SUCCESS", response.toString())

                val resp : BasicResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> patchBoardView.onPatchBoardSuccess(resp.result)  // 성공
                    else -> patchBoardView.onPatchBoardFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("PATCH BOARD / FAILURE", t.message.toString())
            }

        })  // 게시글 사진 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}