package com.example.eraofband.data

import java.io.Serializable

data class User (
    var birth : String,
    var gender : String,
    var introduction : String?,
    var nickName : String,
    var profileImgUrl : String,
    var region : String,
    var userSession : Int
) : Serializable
