package com.example.eraofband.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Band(
    var bandContent: String,
    var bandImgUrl: String,
    var bandIntroduction: String,
    var bandRegion: String,
    var bandTitle: String,
    var base: Int,
    var baseComment: String,
    var chatRoomLink: String,
    var drum: Int,
    var drumComment: String,
    var guitar: Int,
    var guitarComment: String,
    var keyboard: Int,
    var keyboardComment: String,
    @SerializedName("performDate") @Expose var performDate : String?,
    @SerializedName("performFee") @Expose var performFee : Int?,
    @SerializedName("performLocation") @Expose var performLocation : String?,
    @SerializedName("performTime") @Expose var performTime: String?,
    @SerializedName("performTitle") @Expose var performTitle: String?,
    var userIdx: Int,
    var vocal: Int,
    var vocalComment: String
)
