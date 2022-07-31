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
    var nickName: String,
    var profileImgUrl: String,
    var userIdx: Int
)

data class FollowingInfo(
    var nickName: String,
    var profileImgUrl: String,
    var userIdx: Int
)