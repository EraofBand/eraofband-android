package com.example.eraofband.remote

import com.example.eraofband.data.*
import com.example.eraofband.remote.lesson.applyLesson.ApplyLessonResponse
import com.example.eraofband.remote.band.applyBand.ApplyBandResponse
import com.example.eraofband.remote.band.applyDecision.AcceptApplyResponse
import com.example.eraofband.remote.band.applyDecision.RejectApplyResponse
import com.example.eraofband.remote.band.bandLike.BandLikeDeleteResponse
import com.example.eraofband.remote.band.bandLike.BandLikeResponse
import com.example.eraofband.remote.band.getBand.GetBandResponse
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandResponse
import com.example.eraofband.remote.band.getNewBand.GetNewBandResponse
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandResponse
import com.example.eraofband.remote.band.makeBand.MakeBandResponse
import com.example.eraofband.remote.lesson.getLikeLessonList.GetLessonLikeListResponse
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoResponse
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListResponse
import com.example.eraofband.remote.lesson.makeLesson.MakeLessonResponse
import com.example.eraofband.remote.lesson.patchLesson.PatchLessonResponse
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeDeleteResponse
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeResponse
import com.example.eraofband.remote.portfolio.deletePofol.DeletePofolResponse
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolResponse
import com.example.eraofband.remote.portfolio.makePofol.MakePofolResponse
import com.example.eraofband.remote.portfolio.patchPofol.PatchPofolResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentDeleteResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentWriteResponse
import com.example.eraofband.remote.portfolio.pofolLike.PofolDeleteLikeResponse
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeResponse
import com.example.eraofband.remote.sendimg.SendImgResponse
import com.example.eraofband.remote.user.checkUser.CheckUserResponse
import com.example.eraofband.remote.user.getMyPage.GetMyPageResponse
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserResponse
import com.example.eraofband.remote.user.kakaologin.KakaoLoginResponse
import com.example.eraofband.remote.user.patchSession.PatchSessionResponse
import com.example.eraofband.remote.user.patchUser.PatchUserResponse
import com.example.eraofband.remote.user.signout.ResignResponse
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollowList.UserFollowListResponse
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowResponse
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

    //밴드 생성 등록
    @POST("/sessions")
    fun makeBand(@Header("X-ACCESS-TOKEN") jwt : String, @Body band: Band) : Call<MakeBandResponse>

    // 밴드 정보 조회
    @GET("/sessions/info/{bandIdx}")
    fun getBand(@Header("X-ACCESS-TOKEN") jwt: String, @Path("bandIdx") bandIdx: Int) : Call<GetBandResponse>

    // 새로 생성된 밴드 6개 조회
    @GET("/sessions/home/new")
    fun getNewBand() : Call<GetNewBandResponse>

    // Top3 밴드 조회
    @GET("/sessions/home/fame")
    fun getPopularBand() : Call<GetPopularBandResponse>

    // 밴드 좋아요
    @POST("/sessions/likes/{bandIdx}")
    fun bandLike(@Header("X-ACCESS-TOKEN") jwt : String, @Path("bandIdx") bandIdx : Int) : Call<BandLikeResponse>

    // 밴드 좋아요 취소
    @DELETE("/sessions/unlikes/{bandIdx}")
    fun bandLikeDelete(@Header("X-ACCESS-TOKEN") jwt : String, @Path("bandIdx") bandIdx : Int) : Call<BandLikeDeleteResponse>

    // 세션 지원하기
    @POST("/sessions/{bandIdx}")
    fun applyBand(@Header("X-ACCESS-TOKEN") jwt: String, @Path("bandIdx") bandIdx: Int, @Body session: Int) : Call<ApplyBandResponse>

    // 세션 지원 수락하기
    @PATCH("/sessions/in/{bandIdx}/{userIdx}")
    fun acceptApply(@Path("bandIdx") bandIdx: Int, @Path("userIdx") userIdx: Int) : Call<AcceptApplyResponse>

    // 세션 지원 거절하기
    @PATCH("/sessions/out/{bandIdx}/{userIdx}")
    fun rejectApply(@Path("bandIdx") bandIdx: Int, @Path("userIdx") userIdx: Int) : Call<RejectApplyResponse>

    // 찜한 밴드 불러오기
    @GET("/sessions/info/likes")
    fun getLikedBand(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetLikedBandResponse>

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
    fun userFollowList(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<UserFollowListResponse>

    // 레슨 생성
    @POST("/lessons")
    fun makeLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Body lesson: Lesson) : Call<MakeLessonResponse>

    // 레슨 정보 반환
    @GET("/lessons/info/{lessonIdx}")
    fun getLessonInfo(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<GetLessonInfoResponse>

    //지역-세션 분류 레슨 리스트트 반환
    @GET("/lessons/info/list/{lesson-region}/{lesson-session}")
    fun getLessonList(@Path("lesson-region") lessonRegion: String, @Path("lesson-session") lessonSession: Int) : Call<GetLessonListResponse>

    // 레슨 수정
    @PATCH("/lessons/lesson-info/{lessonIdx}")
    fun patchLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int, @Body lesson: Lesson) : Call<PatchLessonResponse>

    // 레슨 지원
    @POST("/lessons/{lessonIdx}")
    fun applyLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<ApplyLessonResponse>

    // 레슨 좋아요
    @POST("/lessons/likes/{lessonIdx}")
    fun lessonLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<LessonLikeResponse>

    // 레슨 좋아요 취소
    @DELETE("/lessons/unlikes/{lessonIdx}")
    fun lessonLikeDelete(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<LessonLikeDeleteResponse>

    // 찜한 레슨 정보 반환
    @GET("/lessons/info/likes")
    fun getLessonLikeList(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetLessonLikeListResponse>
}