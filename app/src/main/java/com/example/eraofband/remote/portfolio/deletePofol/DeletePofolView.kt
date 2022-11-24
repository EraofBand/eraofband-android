package com.example.eraofband.remote.portfolio.deletePofol

import com.example.eraofband.remote.BasicResponse

interface DeletePofolView {
    fun onDeleteSuccess(code: Int, result: String)
    fun onDeleteFailure(response: BasicResponse)
}