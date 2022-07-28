package com.example.eraofband.data

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
    var userIdx: Int,
    var vocal: Int,
    var vocalComment: String
)
