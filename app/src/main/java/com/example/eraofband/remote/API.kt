package com.example.eraofband.remote


import com.example.eraofband.data.*
import com.example.eraofband.remote.checkUser.CheckUserResponse
import com.example.eraofband.remote.deletePofol.DeletePofolResponse
import com.example.eraofband.remote.getLessonList.GetLessonListResponse
import com.example.eraofband.remote.getLessonList.GetLessonListResult
import com.example.eraofband.remote.patchSession.PatchSessionResponse
import com.example.eraofband.remote.getMyPage.GetMyPageResponse
import com.example.eraofband.remote.getMyPofol.GetMyPofolResponse
import com.example.eraofband.remote.getotheruser.GetOtherUserResponse
import com.example.eraofband.remote.kakaologin.KakaoLoginResponse
import com.example.eraofband.remote.makeLesson.MakeLessonResponse
import com.example.eraofband.remote.makeLesson.MakeLessonService
import com.example.eraofband.remote.makePofol.MakePofolResponse
import com.example.eraofband.remote.patchPofol.PatchPofolResponse
import com.example.eraofband.remote.patchPofol.PatchPofolView
import com.example.eraofband.remote.patchuser.PatchUserResponse
import com.example.eraofband.remote.portfolio.*
import com.example.eraofband.remote.sendimg.SendImgResponse
import com.example.eraofband.remote.signout.ResignResponse
import com.example.eraofband.remote.userfollow.UserFollowResponse
import com.example.eraofband.remote.userfollowlist.UserFollowListResponse
import com.example.eraofband.remote.userunfollow.UserUnfollowResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

// 앱을 사용할 때 서버와 통신이 이루어지는 순서로 작성하였습니다
interface API {
    // 이미지 전송
    @Multipart
    @POST("/api/v1/upload")
    fun sendImg(@Part url: MultipartBody.Part) : Call<SendImgResponse>

    // 카카오 로그인
    @POST("/users/signin/{access-token}")
    fun kakaoLogin(@Body user: User, @Path("access-token") token : String) : Call<KakaoLoginResponse>

    // 가입된 유저인지 확인
    @POST("/users/login/{kakao-email}")
    fun checkUser(@Path("kakao-email") email : String) : Call<CheckUserResponse>

    // 마이페이지 정보 조회
    @GET("/users/info/my-page/{userIdx}")
    fun getMyInfo(@Header("X-ACCESS-TOKEN") jwt : String, @Path("userIdx") userIdx : Int) : Call<GetMyPageResponse>

    // 회원 정보 변경
    @PATCH("/users/user-info")
    fun patchUser(@Header("X-ACCESS-TOKEN") jwt: String, @Body user: EditUser): Call<PatchUserResponse>

    // 회원 세션 변경
    @PATCH("/users/user-session")
    fun patchSession(@Header("X-ACCESS-TOKEN") jwt : String, @Body session: Session): Call<PatchSessionResponse>

    // 회원 탈퇴
    @PATCH("/users/status/{userIdx}")
    fun resign(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<ResignResponse>

    // 내 포트폴리오 리스트 조회
    @GET("/pofols/info/{userIdx}")
    fun getMyPofol(@Path("userIdx") userIdx: Int) : Call<GetMyPofolResponse>

    // 내 포트폴리오 등록
    @POST("/pofols")
    fun makePofol(@Header("X-ACCESS-TOKEN") jwt: String, @Body portfolio: Portfolio) : Call<MakePofolResponse>

    // 내 포트폴리오 수정
    @PATCH("/pofols/pofol-info/{pofolIdx}/")
    fun patchPofol(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int, @Body portfolio: Portfolio) : Call<PatchPofolResponse>

    // 내 포트폴리오 삭제
    @PATCH("/pofols/status/{pofolIdx}")
    fun deletePofol(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int, @Body userIdx: Int) : Call<DeletePofolResponse>

    // 포트폴리오 좋아요
    @POST("/pofols/likes/{pofolIdx}")
    fun pofolLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolLikeResponse>

    // 포트폴리오 좋아요 취소
    @DELETE("/pofols/unlikes/{pofolIdx}")
    fun pofolDeleteLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolDeleteLikeResponse>

    // 포트폴리오 댓글 불러오기
    @GET("/pofols/info/comment")
    fun pofolComment(@Query("pofolIdx") pofolIdx: Int) : Call<PofolCommentResponse>

    // 포트폴리오 댓글 달기
    @POST("/pofols/comment/{pofolIdx}")
    fun pofolWriteComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int, @Body comment: Comment) : Call<PofolCommentWriteResponse>

    // 포트폴리오 댓글 삭제하기
    @PATCH("/pofols/comment/status/{pofolCommentIdx}")
    fun pofolDeleteComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolCommentIdx") commentIdx: Int, @Body userIdx: Int) : Call<PofolCommentDeleteResponse>

    // 다른 유저 정보 불러오기
    @GET("/users/info/{userIdx}")
    fun getUser(@Header("X-ACCESS-TOKEN") jwt : String, @Path("userIdx") userIdx : Int) : Call<GetOtherUserResponse>

    // 유저 팔로우 처리
    @POST("/users/follow/{userIdx}")
    fun userFollow(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<UserFollowResponse>

    // 유저 팔로우 취소
    @DELETE("/users/unfollow/{userIdx}")
    fun userUnfollow(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<UserUnfollowResponse>

    // 유저 팔로우 리스트 불러오기
    @GET("/users/info/follow/{userIdx}")
    fun userFollowList(@Path("userIdx") userIdx: Int) : Call<UserFollowListResponse>

    // 레슨 생성
    @POST("/lessons")
    fun makeLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Body lesson: Lesson) : Call<MakeLessonResponse>

    //지역-세션 분류 레슨 정보 반환
    @GET("/lessons/info/list/{lesson-region}/{lesson-session}")
    fun getLessonList(@Path("lesson-region") lessonRegion: String, @Path("lesson-session") lessonSession: Int) : Call<GetLessonListResponse>
}