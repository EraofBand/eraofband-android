package com.example.eraofband.remote.deletePofol

interface DeletePofolView {
    fun onDeleteSuccess(code: Int, result: String)
    fun onDeleteFailure(response: DeletePofolResponse)
}