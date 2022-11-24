package com.example.eraofband.remote.search.getBand

import android.util.Log
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResponse
import com.example.eraofband.remote.search.getLesson.GetSearchLessonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetSearchBandService {
    private lateinit var getBandView: GetSearchBandView

    fun setBandView(getBandView: GetSearchBandView) {
        this.getBandView = getBandView
    }

    fun getSearchBand(keyword : String) {  // 새로 생성된 밴드 6개 리스트 조회
        val getSearchBandService =  NetworkModule().getRetrofit()?.create(API::class.java)

        getSearchBandService?.getSearchBand(keyword)?.enqueue(object :
            Callback<GetSearchBandResponse> {
            override fun onResponse(call: Call<GetSearchBandResponse>, response: Response<GetSearchBandResponse>) {
                // 응답이 왔을 때 처리
                Log.d("GET BAND / SUCCESS", response.toString())

                val resp : GetSearchBandResponse = response.body()!!

                when(val code = resp.code) {
                    1000 -> getBandView.onGetSearchBandSuccess(resp.result)  // 성공
                    else -> getBandView.onGetSearchBandFailure(code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetSearchBandResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("GET BAND / FAILURE", t.message.toString())
            }

        })  // 밴드 정보를 넣어주면서 api 호출, enqueue에서 응답 처리
    }
}