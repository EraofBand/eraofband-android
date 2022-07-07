package com.example.eraofband.data

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName(value = "birth") var birth : String,
    @SerializedName(value = "gender") var gender : String,
    @SerializedName(value = "nickName") var nickName : String,
    @SerializedName(value = "profileImgUrl") var profileImgUrl : String,
    @SerializedName(value = "region") var region : String,
    @SerializedName(value = "session") var session : Int
)
