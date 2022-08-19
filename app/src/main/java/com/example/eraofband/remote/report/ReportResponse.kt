package com.example.eraofband.remote.report

data class ReportResponse (
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: String
)