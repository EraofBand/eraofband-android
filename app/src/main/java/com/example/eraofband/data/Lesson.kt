package com.example.eraofband.data

data class Lesson(
    var capacity: Int,              // 수강생 모집 인원
    var chatRoomLink: String,       // 채팅방 링크 URL
    var lessonContent: String,      // 레슨 소개 내용
    var lessonImgUrl: String,       // 레슨 이미지
    var lessonIntroduction: String, // 레슨 한 줄 소개
    var lessonRegion: String,       // 레슨 지역
    var lessonSession: Int,         // 레슨 종목(세션)
    var lessonTitle: String,        // 레슨 제목
    var userIdx: Int                // 강사 유저 인덱스
)
