package com.example.eraofband.remote


import com.example.eraofband.data.Comment
import com.example.eraofband.data.Session
import com.example.eraofband.data.User
import com.example.eraofband.remote.checkUser.CheckUserResponse
import com.example.eraofband.remote.patchSession.PatchSessionResponse
import com.example.eraofband.remote.getMyPage.GetMyPageResponse
import com.example.eraofband.remote.getMyPofol.GetMyPofolResponse
import com.example.eraofband.remote.getuser.GetUserResponse
import com.example.eraofband.remote.kakaologin.KakaoLoginResponse
import com.example.eraofband.remote.patchuser.PatchUserResponse
import com.example.eraofband.remote.portfolio.*
import com.example.eraofband.remote.signout.ResignResponse
import retrofit2.Call
import retrofit2.http.*

// 앱을 사용할 때 서버와 통신이 이루어지는 순서로 작성하였습니다
interface API {
    // 카카오 로그인
    @POST("/users/signin/{access-token}")
    fun kakaoLogin(@Body user: User, @Path("access-token") token : String) : Call<KakaoLoginResponse>

    // 가입된 유저인지 확인
    @PATCH("/users/login/{kakao-email}")
    fun checkUser(@Path("kakao-email") email : String) : Call<CheckUserResponse>

    // 마이페이지 정보 조회
    @GET("/users/my-page/{userIdx}")
    fun getUser(@Header("X-ACCESS-TOKEN") jwt : String, @Path("userIdx") userIdx : Int) : Call<GetMyPageResponse>

    // 회원 정보 변경
    @PATCH("/users/user-info")
    fun patchUser(@Header("X-ACCESS-TOKEN") jwt : String, @Body user : User): Call<PatchUserResponse>

    // 회원 세션 변경
    @PATCH("/users/user-session")
    fun patchSession(@Header("X-ACCESS-TOKEN") jwt : String, @Body session: Session): Call<PatchSessionResponse>

    // 회원 탈퇴
    @PATCH("/users/delete/{userIdx}")
    fun resign(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<ResignResponse>

    // 내 포트폴리오 리스트 조회
    @GET("/pofol/my/")
    fun getMyPofol(@Query("userIdx") userIdx: Int) : Call<GetMyPofolResponse>

    // 포트폴리오 좋아요
    @POST("/pofol/{pofolIdx}/likes")
    fun pofolLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolLikeResponse>

    // 포트폴리오 좋아요 취소
    @DELETE("/pofol/{pofolIdx}/unlikes")
    fun pofolDeleteLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx : Int) : Call<PofolDeleteLikeResponse>

    // 포트폴리오 댓글 불러오기
    @GET("/pofol/comment/")
    fun pofolComment(@Query("pofolIdx") pofolIdx: Int) : Call<PofolCommentResponse>

    // 포트폴리오 댓글 달기
    @POST("/pofol/{pofolIdx}/comment")
    fun pofolWriteComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int, @Body comment: Comment) : Call<PofolCommentWriteResponse>

    // 포트폴리오 댓글 삭제하기
    @PATCH("/pofol/{pofolCommentIdx}/comment/status")
    fun pofolDeleteComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolCommentIdx") commentIdx: Int, @Query("userIdx") userIdx: Int) : Call<PofolCommentDeleteResponse>

    // 다른회원 정보 조회
    @GET("/users/{userIdx}")
    fun getUser(@Path("userIdx") userIdx : Int) : Call<GetUserResponse>

}