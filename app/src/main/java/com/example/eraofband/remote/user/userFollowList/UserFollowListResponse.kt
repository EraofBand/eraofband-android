package com.example.eraofband.remote.user.userFollowList

data class UserFollowListResponse(
    val code: Int,
    val onSuccess: Boolean,
    val message: String,
    val result: UserFollowListResult
)

data class UserFollowListResult(
    var getfollower: List<FollowerInfo>,
    var getfollowing: List<FollowingInfo>
)

data class FollowerInfo(
    var follow: Int,
    var nickName: String,
    var profileImgUrl: String,
    var token: String,
    var userIdx: Int
)

data class FollowingInfo(
    var follow: Int,
    var nickName: String,
    var profileImgUrl: String,
    var token: String,
    var userIdx: Int
)