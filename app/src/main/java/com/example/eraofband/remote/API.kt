package com.example.eraofband.remote

import com.example.eraofband.data.*
import com.example.eraofband.remote.band.applyBand.ApplyBandResponse
import com.example.eraofband.remote.band.applyDecision.AcceptApplyResponse
import com.example.eraofband.remote.band.applyDecision.RejectApplyResponse
import com.example.eraofband.remote.band.bandLike.BandLikeDeleteResponse
import com.example.eraofband.remote.band.bandLike.BandLikeResponse
import com.example.eraofband.remote.band.deleteBand.DeleteBandResponse
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandResponse
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandResponse
import com.example.eraofband.remote.band.getBand.GetBandResponse
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandResponse
import com.example.eraofband.remote.band.getNewBand.GetNewBandResponse
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandResponse
import com.example.eraofband.remote.band.getRegionBand.GetRegionBandResponse
import com.example.eraofband.remote.band.makeAlbumBand.MakeAlbumBandResponse
import com.example.eraofband.remote.band.makeBand.MakeBandResponse
import com.example.eraofband.remote.band.patchBand.PatchBandResponse
import com.example.eraofband.remote.board.deleteBoardImg.DeleteBoardImgResponse
import com.example.eraofband.remote.board.getBoardList.GetBoardListResponse
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResponse
import com.example.eraofband.remote.board.getMyCommentList.GetMyCommentListResponse
import com.example.eraofband.remote.board.patchBoard.PatchBoardResponse
import com.example.eraofband.remote.board.postBoard.PostBoardResponse
import com.example.eraofband.remote.board.postBoardImg.PostBoardImgResponse
import com.example.eraofband.remote.board.boardComment.BoardDeleteCommentResponse
import com.example.eraofband.remote.board.boardComment.BoardWriteCommentResponse
import com.example.eraofband.remote.board.boardLike.BoardDeleteLikeResponse
import com.example.eraofband.remote.board.boardLike.BoardLikeResponse
import com.example.eraofband.remote.board.deleteBoard.DeleteBoardResponse
import com.example.eraofband.remote.board.getBoard.GetBoardResponse
import com.example.eraofband.remote.chat.activeChat.ActiveChatResponse
import com.example.eraofband.remote.chat.getChatList.GetChatListResponse
import com.example.eraofband.remote.chat.isChatRoom.IsChatRoomResponse
import com.example.eraofband.remote.chat.makeChat.MakeChatResponse
import com.example.eraofband.remote.chat.patchChat.PatchChatResponse
import com.example.eraofband.remote.lesson.applyLesson.ApplyLessonResponse
import com.example.eraofband.remote.lesson.deleteLesson.DeleteLessonResponse
import com.example.eraofband.remote.lesson.deleteUserLesson.DeleteUserLessonResponse
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoResponse
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListResponse
import com.example.eraofband.remote.lesson.getLikeLessonList.GetLessonLikeListResponse
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeDeleteResponse
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeResponse
import com.example.eraofband.remote.lesson.makeLesson.MakeLessonResponse
import com.example.eraofband.remote.lesson.patchLesson.PatchLessonResponse
import com.example.eraofband.remote.notice.deleteNotice.DeleteNoticeResponse
import com.example.eraofband.remote.notice.getNewNotice.GetNewNoticeResponse
import com.example.eraofband.remote.notice.getNotice.GetNoticeResponse
import com.example.eraofband.remote.portfolio.deletePofol.DeletePofolResponse
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResponse
import com.example.eraofband.remote.portfolio.makePofol.MakePofolResponse
import com.example.eraofband.remote.portfolio.patchPofol.PatchPofolResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentDeleteResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentResponse
import com.example.eraofband.remote.portfolio.pofolComment.PofolCommentWriteResponse
import com.example.eraofband.remote.portfolio.pofolLike.PofolDeleteLikeResponse
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeResponse
import com.example.eraofband.remote.report.ReportResponse
import com.example.eraofband.remote.search.getBand.GetSearchBandResponse
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResponse
import com.example.eraofband.remote.search.getUser.GetSearchUserResponse
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

    // 전체 포트폴리오 리스트 조회
    @GET("/pofols/info/all/{pofolIdx}")
    fun getPofol(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int) : Call<GetPofolResponse>

    // 팔로우 한 유저 포트폴리오 리스트 조회
    @GET("/pofols/info/follow/{pofolIdx}")
    fun getFollowPofol(@Header("X-ACCESS-TOKEN") jwt: String, @Path("pofolIdx") pofolIdx: Int) : Call<GetPofolResponse>

    // 게시물 리스트 조회
    @GET("/board/list/info/{category}/{boardIdx}")
    fun getBoardList(@Path("category") category: Int, @Path("boardIdx") boardIdx: Int) : Call<GetBoardListResponse>

    // 작성 게시물 리스트 조회
    @GET("/board/my")
    fun getMyBoardList(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetMyBoardListResponse>

    // 댓글 단 게시물 리스트 조회
    @GET("/board/my-comment")
    fun getMyCommentList(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetMyCommentListResponse>

    // 게시물 정보 조회
    @GET("/board/info/{boardIdx}")
    fun getBoard(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int) : Call<GetBoardResponse>

    // 게시물 댓글 등록
    @POST("/board/comment/{boardIdx}")
    fun writeBoardComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int, @Body comment: Comment) : Call<BoardWriteCommentResponse>

    // 게시물 댓글 삭제
    @PATCH("/board/comment/status/{boardCommentIdx}")
    fun deleteBoardComment(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardCommentIdx") boardCommentIdx: Int, @Body userIdx: Int) : Call<BoardDeleteCommentResponse>

    // 게시물 답글 등록
    @POST("/board/re-comment/{boardIdx}")
    fun writeBoardReply(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int, @Body reply: Reply) : Call<BoardWriteCommentResponse>

    // 게시물 좋아요
    @POST("/board/likes/{boardIdx}")
    fun likeBoard(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int) : Call<BoardLikeResponse>

    // 게시물 좋아요 취소
    @DELETE("/board/unlikes/{boardIdx}")
    fun deleteBoardLike(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int) : Call<BoardDeleteLikeResponse>

    // 게시물 삭제
    @PATCH("/board/status/{boardIdx}")
    fun deleteBoard(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int, @Body userIdx: Int) : Call<DeleteBoardResponse>

    // 내 포트폴리오 리스트 조회
    @GET("/pofols/info/{userIdx}")
    fun getMyPofol(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<GetPofolResponse>

    // 게시판 게시물 사진 수정


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

    //밴드 생성 등록
    @POST("/sessions")
    fun makeBand(@Header("X-ACCESS-TOKEN") jwt : String, @Body band:Band) : Call<MakeBandResponse>

    //밴드 수정
    @PATCH("/sessions/band-info/{bandIdx}")
    fun patchBand(@Header("X-ACCESS-TOKEN") jwt : String, @Path("bandIdx") bandIdx : Int, @Body band: Band) : Call<PatchBandResponse>

    //지역-세션 분류 밴드 정보 반환
    @GET("/sessions/info/list/{band-region}/{band-session}")
    fun getRegionBand(@Path("band-region") bandRegion : String, @Path("band-session") bandSession : Int) : Call<GetRegionBandResponse>

    // 밴드 삭제
    @PATCH("/sessions/status/{bandIdx}")
    fun deleteBand(@Header("X-ACCESS-TOKEN") jwt: String, @Path("bandIdx") bandIdx: Int, @Body userIdx: Int) : Call<DeleteBandResponse>

    // 밴드 탈퇴
    @DELETE("/sessions/out/{bandIdx}")
    fun deleteUserBand(@Header("X-ACCESS-TOKEN") jwt: String, @Path("bandIdx") bandIdx: Int) : Call<DeleteUserBandResponse>

    // 밴드 앨범 리스트 반환
    @GET("/sessions/album/info/{bandIdx}")
    fun getAlbumBand(@Header("X-ACCESS-TOKEN") jwt: String, @Path("bandIdx") bandIdx: Int) : Call<GetAlbumBandResponse>

    // 앨범 생성
    @POST("/sessions/album")
    fun makeAlbum(@Header("X-ACCESS-TOKEN") jwt: String, @Body album: Album) : Call<MakeAlbumBandResponse>

    // 레슨 생성
    @POST("/lessons")
    fun makeLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Body lesson: Lesson) : Call<MakeLessonResponse>

    // 레슨 정보 반환
    @GET("/lessons/info/{lessonIdx}")
    fun getLessonInfo(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<GetLessonInfoResponse>

    // 지역-세션 분류 레슨 리스트 반환
    @GET("/lessons/info/list/{lesson-region}/{lesson-session}")
    fun getLessonList(@Path("lesson-region") lessonRegion: String, @Path("lesson-session") lessonSession: Int) : Call<GetLessonListResponse>

    // 레슨 수정
    @PATCH("/lessons/lesson-info/{lessonIdx}")
    fun patchLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int, @Body lesson: Lesson) : Call<PatchLessonResponse>

    // 레슨 삭제
    @PATCH("/lessons/status/{lessonIdx}")
    fun deleteLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int, @Body userIdx: Int) : Call<DeleteLessonResponse>

    // 레슨 탈퇴
    @DELETE("/lessons/out/{lessonIdx}")
    fun deleteUserLesson(@Header("X-ACCESS-TOKEN") jwt: String, @Path("lessonIdx") lessonIdx: Int) : Call<DeleteUserLessonResponse>

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

    // 알림 리스트 조회
    @GET("/notice/{userIdx}")
    fun getNotice(@Header("X-ACCESS-TOKEN") jwt: String, @Path("userIdx") userIdx: Int) : Call<GetNoticeResponse>

    // 홈 화면 새 알림 여부
    @GET("/notice/alarm")
    fun getNewNotice(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetNewNoticeResponse>

    // 알림 전체 삭제
    @DELETE("/notice/status")
    fun deleteNotice(@Header("X-ACCESS-TOKEN") jwt: String) : Call<DeleteNoticeResponse>

    // 유저 검색
    @GET("/search/users/{keyword}")
    fun getSearchUser(@Path("keyword") keyword: String) : Call<GetSearchUserResponse>

    // 밴드 검색
    @GET("/search/bands/{keyword}")
    fun getSearchBand(@Path("keyword") keyword: String) : Call<GetSearchBandResponse>

    // 레슨 검색
    @GET("/search/lessons/{keyword}")
    fun getSearchLesson(@Path("keyword") keyword: String) : Call<GetSearchLessonResponse>

    // 채팅방 리스트 조회
    @GET("/chat/chat-room")
    fun getChatList(@Header("X-ACCESS-TOKEN") jwt: String) : Call<GetChatListResponse>

    // 채팅방 존재 유무 조회
    @PATCH("/chat/chat-room/exist")
    fun isChatRoom(@Header("X-ACCESS-TOKEN") jwt: String, @Body users: Users) : Call<IsChatRoomResponse>

   // 채팅방 나가기 처리
    @PATCH("/chat/status/{chatRoomIdx}")
    fun patchChat(@Header("X-ACCESS-TOKEN") jwt: String, @Path("chatRoomIdx") chatRoomIdx: String) : Call<PatchChatResponse>

    // 채팅방 생성 처리
    @POST("/chat")
    fun makeChat(@Body makeChatRoom: MakeChatRoom) : Call<MakeChatResponse>

    // 채팅방 활성화
    @PATCH("/chat/status/active")
    fun activeChat(@Header("X-ACCESS-TOKEN") jwt : String, @Body makeChatRoom: MakeChatRoom) : Call<ActiveChatResponse>

    // 게시물 생성
    @POST("/board")
    fun postBoard(@Header("X-ACCESS-TOKEN") jwt: String, @Body board : Board) : Call<PostBoardResponse>

    // 게시물 사진 수정
    @POST("/board/board-img/{boardIdx}")
    fun postBoardImg(@Path("boardIdx") boardIdx: Int, @Body imgUrl : String) : Call<PostBoardImgResponse>

    // 게시물 수정
    @PATCH("/board/board-info/{boardIdx}")
    fun patchBoard(@Header("X-ACCESS-TOKEN") jwt: String, @Path("boardIdx") boardIdx: Int, @Body boardContent : BoardContent) : Call<PatchBoardResponse>

    //게시물 사진 삭제
    @PATCH("/board/status-img/{boardImgIdx}")
    fun deleteBoardImg(@Path("boardImgIdx") boardImgIdx : Int) : Call<DeleteBoardImgResponse>

    // 신고하기
    @POST("/notice/report")
    fun report(@Header("X-ACCESS-TOKEN") jwt: String, @Body report: Report) : Call<ReportResponse>

}