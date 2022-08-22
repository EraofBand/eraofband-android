package com.example.eraofband.remote.report

import android.util.Log
import com.example.eraofband.data.Report
import com.example.eraofband.remote.API
import com.example.eraofband.remote.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportService {
    private lateinit var reportView : ReportView

    fun setReportView(reportView: ReportView) {
        this.reportView = reportView
    }
    fun report(jwt: String, report: Report) {  // 좋아요
        val reportService =  NetworkModule().getRetrofit()?.create(API::class.java)

        reportService?.report(jwt, report)?.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                // 응답이 왔을 때 처리
                Log.d("REPORT / SUCCESS", response.toString())

                val resp : ReportResponse = response.body()!!

                when(resp.code) {
                    1000 -> reportView.onReportSuccess(resp.result)  // 성공
                    else -> reportView.onReportFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                // 네트워크 연결이 실패했을 때 실행
                Log.d("REPORT / FAILURE", t.message.toString())
            }

        })  // 유저 정보를 넣어주면서 api 호출, eunqueue에서 응답 처리
    }
}