package com.example.eraofband.data


data class Board(
    var category: Int,
    var content: String,
    var postImgUrl: ArrayList<PostImgUrl>,
    var title: String,
    var userIdx: Int
)

data class PostImgUrl(
    var imgUrl : String
)
