package com.example.eraofband.remote.pofollike

import retrofit2.Call
import retrofit2.http.*

interface PofolLikeInterface {
    @POST("/pofol/{pofolIdx}/likes")  // 좋아요
    fun pofolLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolLikeResponse>

    @DELETE("/pofol/{pofolIdx}/unlikes")  // 좋아요 취소
    fun pofolDeleteLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolDeleteLikeResponse>

}