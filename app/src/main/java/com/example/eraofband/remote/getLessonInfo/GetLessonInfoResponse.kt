package com.example.eraofband.remote.getLessonInfo

data class GetLessonInfoResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: GetLessonInfoResult
)

data class GetLessonInfoResult(
    var capacity: Int,  // 수용인원
    var chatRoomLink: String,  // 채팅방 링크
    var lessonContent: String,  // 레슨 소개
    var lessonIdx: Int,  // 레슨 인덱스
    var lessonImgUrl: String,  // 레슨 이미지
    var lessonIntroduction: String,  // 레슨 한 줄 소개
    var lessonLikeCount: Int,  // 레슨 찜 개수
    var lessonMembers: List<LessonMembers>,  // 레슨 수강생 리스트
    var lessonRegion: String,  // 레슨 지역
    var lessonSession: Int,  // 레슨 종목
    var lessonTitle: String,  // 레슨 타이틀
    var likeOrNot: String,  // Y or N
    var memberCount: Int,  // 멤버 수
    var nickName: String,  // 강사 닉네임
    var profileImgUrl: String,  // 강사 프사
    var userIdx: Int,          // 강사 유저인덱스
    var userIntroduction: String  // 강사 한 줄 소개
)

data class LessonMembers(
    var introduction: String,
    var mySession: Int,
    var nickName: String,
    var profileImgUrl: String,
    var userIdx: Int
)