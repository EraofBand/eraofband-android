package com.example.eraofband.data

data class Portfolio (
    var pofolIdx : Int, // 얘도 좋아요 실험을 위해서 만들었습니다 포폴 연동되면 삭제할게요!!
    var content : String,
    var imgUrl : Int,  // 나중에 String으로 바꿉시당, 이게 프사였네요
    var title : String,
    var userIdx : Int,
    var vidioUrl : String,
    var likeOrNot : String  // 얘는 좋아요 실험을 위해서 만들어 놓은거라 포폴 연동되면 삭제할거에요!!
)
