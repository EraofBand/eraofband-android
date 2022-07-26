package com.example.eraofband.remote.getBand

import com.example.eraofband.remote.getNewBand.GetNewBandResult

data class GetBandResponse(
    var code: Int,
    var isSuccess: Boolean,
    var message: String,
    var result: GetBandResult
)

data class GetBandResult(
    var applicants: List<Applicants>,
    var bandContent: String,
    var bandIdx: Int,
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
    var mySession: Int,
    var nickName: String,
    var performDate: String,
    var performFee: Int,
    var performLocation: String,
    var performTime: String,
    var sessionMembers: List<SessionMembers>,
    var userIdx: Int,
    var vocal: Int,
    var vocalComment: String
)

data class Applicants(
    var buSession: Int,
    var introduction: String,
    var nickName: String,
    var profileImgUrl: String,
    var updatedAt: String,
    var userIdx: Int
)

data class SessionMembers(
    var buSession: Int,
    var introduction: String,
    var nickName: String,
    var profileImgUrl: String,
    var userIdx: Int
)