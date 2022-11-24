package com.example.eraofband.remote.report

interface ReportView {
    fun onReportSuccess(result: String)
    fun onReportFailure(code: Int, message:String)
}