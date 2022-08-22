package com.example.eraofband.remote.board.patchBoard

interface PatchBoardView {
    fun onPatchBoardSuccess(result : String)
    fun onPatchBoardFailure(code: Int, message : String)
}