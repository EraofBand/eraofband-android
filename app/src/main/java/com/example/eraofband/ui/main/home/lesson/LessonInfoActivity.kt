package com.example.eraofband.ui.main.home.lesson

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityLessonInfoBinding
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoResult
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoService
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoView
import com.example.eraofband.remote.lesson.getLessonInfo.LessonMembers
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeResult
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeService
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeView
import com.example.eraofband.ui.main.home.session.band.BandDeleteDialog
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import com.example.eraofband.ui.main.report.ReportDialog
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link


class LessonInfoActivity : AppCompatActivity(), GetLessonInfoView, LessonLikeView {
    private lateinit var binding: ActivityLessonInfoBinding
    private lateinit var defaultFeed: FeedTemplate
    private lateinit var lessonStudentRVAdapter: LessonStudentRVAdapter
    private var lessonIdx: Int? = null
    private var teacherIdx: Int? = null
    private var lessonMember = false
    private var isFull = false
    private var studentIdxList = arrayListOf<Int>()
    private var getLessonInfo = GetLessonInfoService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lessonLike = LessonLikeService() // 레슨 좋아요 api
        lessonLike.setLikeView(this)

        binding.homeLessonInfoRl.setOnRefreshListener {

            getLessonInfo.getLessonInfoView(this)
            getLessonInfo.getLessonInfo(getJwt()!!, lessonIdx!!)

            binding.homeLessonInfoRl.isRefreshing = false
        }

        binding.lessonInfoBackIv.setOnClickListener { // 뒤로가기
            finish()
        }

        binding.lessonInfoListIv.setOnClickListener {  // 레슨 수정 클릭리스너
            showPopup(binding.lessonInfoListIv)
        }

        binding.lessonInfoLikeIv.setOnClickListener { // 레슨 좋아요
            binding.lessonInfoLikeIv.visibility = View.INVISIBLE
            binding.lessonInfoUnlikeIv.visibility = View.VISIBLE
            lessonLike.lessonLike(getJwt()!!, lessonIdx!!)
        }

        binding.lessonInfoUnlikeIv.setOnClickListener { // 레슨 좋아요 취소
            binding.lessonInfoLikeIv.visibility = View.VISIBLE
            binding.lessonInfoUnlikeIv.visibility = View.INVISIBLE
            lessonLike.lessonLikeDelete(getJwt()!!, lessonIdx!!)
        }

        binding.lessonInfoApplyTv.setOnClickListener { // 레슨 신청 다이얼로그
            if (isFull) {  // 인원 마감 시
                Toast.makeText(this, "신청 인원이 마감되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val applyDialog = LessonApplyDialog(teacherIdx!!, getJwt()!!, getUserIdx(), lessonIdx!!)
                applyDialog.isCancelable = false
                applyDialog.show(supportFragmentManager, "applicant")
            }
        }

        val intent = Intent(this, UserMyPageActivity::class.java)  // 강사 클릭 시 강사 정보 페이지
        binding.lessonInfoTeacherProfileIv.setOnClickListener {
            if (teacherIdx == getUserIdx()) {
                startActivity(Intent(baseContext, MyPageActivity::class.java))
            } else {
                intent.putExtra("userIdx", teacherIdx)
                startActivity(intent)
            }
        }
        binding.lessonInfoTeacherLy.setOnClickListener {
            if (teacherIdx == getUserIdx()) {
                startActivity(Intent(baseContext, MyPageActivity::class.java))
            } else {
                intent.putExtra("userIdx", teacherIdx)
                startActivity(intent)
            }
        }
        binding.lessonInfoShareTv.setOnClickListener { // 공유하기
            binding.lessonInfoPb.visibility = View.VISIBLE
            // 피드 메시지 보내기
            // 카카오톡 설치여부 확인
            if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
                // 카카오톡으로 카카오톡 공유 가능
                ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
                    if (error != null) {
                        Log.e("SHARE", "카카오톡 공유 실패", error)
                    }
                    else if (sharingResult != null) {
                        Log.d("SHARE", "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)

                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w("SHARE", "Warning Msg: ${sharingResult.warningMsg}")
                        Log.w("SHARE", "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // 카카오톡 미설치: 웹 공유 사용 권장
                // 웹 공유 예시 코드
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

                // CustomTabs으로 웹 브라우저 열기

                // 1. CustomTabsServiceConnection 지원 브라우저 열기
                // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
                try {
                    KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
                } catch(e: UnsupportedOperationException) {
                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                }

                // 2. CustomTabsServiceConnection 미지원 브라우저 열기
                // ex) 다음, 네이버 등
                try {
                    KakaoCustomTabsClient.open(this, sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                binding.lessonInfoPb.visibility = View.GONE
            }, 3000)
        }
    }

    override fun onResume() {
        super.onResume()
        lessonIdx = intent.getIntExtra("lessonIdx", 0)
        getLessonInfo.getLessonInfoView(this)
        getLessonInfo.getLessonInfo(getJwt()!!, lessonIdx!!)
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRecyclerView(studentList: List<LessonMembers>) {
        // 수강생 목록 리사이클러뷰
        lessonStudentRVAdapter = LessonStudentRVAdapter()
        binding.lessonInfoStudentRv.adapter = lessonStudentRVAdapter
        binding.lessonInfoStudentRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lessonStudentRVAdapter.initStudentList(studentList)

        lessonStudentRVAdapter.setMyItemClickListener(object :
            LessonStudentRVAdapter.MyItemClickListener {
            override fun userInfo(userIdx: Int) {

                val intent = Intent(baseContext, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }
        })
    }

    private fun showPopup(view: View) {
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.lesson_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            when(item!!.itemId) {
                R.id.lesson_edit -> {  // 레슨 수정
                    val intent = Intent(this, LessonEditActivity::class.java)
                    intent.putExtra("lessonIdx", lessonIdx)
                    startActivity(intent)
                }
                R.id.lesson_delete -> {  // 레슨 삭제
                    val deleteDialog = BandDeleteDialog(getJwt()!!, getUserIdx(), lessonIdx!!)
                    deleteDialog.show(supportFragmentManager, "deleteLesson")
                }
                R.id.lesson_leave -> {  // 레슨 탈퇴
                    val deleteDialog = BandDeleteDialog(getJwt()!!, getUserIdx(), lessonIdx!!)
                    deleteDialog.show(supportFragmentManager, "resignLesson")

                }
                else -> {
                    val reportDialog = ReportDialog(getJwt()!!, 4, lessonIdx!!, teacherIdx!!)
                    reportDialog.isCancelable = false
                    reportDialog.show(supportFragmentManager, "report")
                }
            }
            false
        }

        if(getUserIdx() == teacherIdx){
            popupMenu.menu.setGroupVisible(R.id.lesson_report_gr, false)
            popupMenu.menu.setGroupVisible(R.id.lesson_leave_gr, false)
        } else if(lessonMember) {
            popupMenu.menu.setGroupVisible(R.id.lesson_report_gr, false)
            popupMenu.menu.setGroupVisible(R.id.lesson_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.lesson_delete_gr, false)
        } else{
            popupMenu.menu.setGroupVisible(R.id.lesson_leave_gr, false)
            popupMenu.menu.setGroupVisible(R.id.lesson_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.lesson_delete_gr, false)
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun checkUserIdx(memberList: List<LessonMembers>): Boolean {
        // 만약 내 userIdx가 멤버 리스트의 userIdx와 같으면 밴드 멤버에 속함
        if(memberList.isEmpty()) return false  // 밴드 멤버가 없으면 무조건 false

        for(element in memberList) {
            if(getUserIdx() == element.userIdx) return true
        }

        return false
    }

    override fun onGetLessonInfoSuccess(
        code: Int,
        result: GetLessonInfoResult
    ) { // 여기에 initRecyclerView
        Log.d("GET/SUCCESS", result.toString())
        teacherIdx = result.userIdx // 강사인덱스 초기화
        studentIdxList.add(teacherIdx!!)

        if (result.memberCount == result.capacity) isFull = true  // 인원 마감인 경우 true

        Glide.with(this)  // 레슨 이미지
            .load(result.lessonImgUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.lessonInfoLessonImgIv)
        binding.lessonInfoLessonImgIv.clipToOutline = true  // 모서리 깎기

        binding.lessonInfoTitleTv.text = result.lessonTitle // 상단바 타이틀
        binding.lessonInfoLessonTitleTv.text = result.lessonTitle // 타이틀
        binding.lessonInfoLessonIntroTv.text = result.lessonIntroduction // 한 줄 소개
        binding.lessonInfoCntTv.text = "${result.memberCount} / ${result.capacity}명" // 인원 수
        binding.lessonInfoAreaTv.text = result.lessonRegion // 레슨 지역
        binding.lessonInfoSessionTv.text = when (result.lessonSession) {  // 레슨 종목
            0 -> "보컬"
            1 -> "기타"
            2 -> "베이스"
            3 -> "키보드"
            else -> "드럼"
        }

        if (result.likeOrNot == "Y") { // 레슨 좋아요요
           binding.lessonInfoLikeIv.visibility = View.INVISIBLE
            binding.lessonInfoUnlikeIv.visibility = View.VISIBLE
        }
        if (result.likeOrNot == "N") {
            binding.lessonInfoLikeIv.visibility = View.VISIBLE
            binding.lessonInfoUnlikeIv.visibility = View.INVISIBLE
        }

        Glide.with(this)  // 강사 이미지
            .load(result.profileImgUrl)
            .apply(RequestOptions.centerCropTransform())
            .apply(RequestOptions.circleCropTransform())
            .into(binding.lessonInfoTeacherProfileIv)

        binding.lessonInfoTeacherNicknameTv.text = result.nickName // 강사 닉네임
        binding.lessonInfoTeacherIntroTv.text = result.userIntroduction  // 강사 한 줄 소개
        binding.lessonInfoIntroTv.text = result.lessonContent // 레슨 소개

        for (idx in result.lessonMembers) { // 멤버 인덱스 리스트 초기화
            studentIdxList.add(idx.userIdx)
        }

        if (studentIdxList.contains(getUserIdx())) {  // 채팅 링크는 수강생에게만 공개
            binding.lessonInfoChatTv.text = result.chatRoomLink
        } else {
            binding.lessonInfoChatTv.text = "수강생에게만 공개됩니다."
        }

        if(result.lessonMembers.isEmpty()) {  // 밴드 멤버가 없는 경우
            binding.lessonInfoStudentRv.visibility = View.GONE
            binding.lessonInfoNoMemberTv.visibility = View.VISIBLE
        }
        else {  // 밴드 멤버가 있는 경우
            binding.lessonInfoStudentRv.visibility = View.VISIBLE
            binding.lessonInfoNoMemberTv.visibility = View.GONE

            initRecyclerView(result.lessonMembers) // 수강생 목록
        }
        lessonMember = checkUserIdx(result.lessonMembers)

        defaultFeed = FeedTemplate(
            content = Content(
                title = result.lessonTitle,
                description = result.lessonIntroduction,
                imageUrl = result.lessonImgUrl,
                link = Link(
                    mobileWebUrl = "https://play.google.com"
                )
            ),
            buttons = listOf(
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf("test" to "test"),
                        iosExecutionParams = mapOf("test" to "test")
                    )
                )
            )
        )
    }


    //뷰 함수들
    override fun onGetLessonInfoFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

    override fun onLikeSuccess(code: Int, result: LessonLikeResult) {
        Log.d("LIKE/SUCCESS", result.toString())
    }

    override fun onLikeFailure(code: Int, message: String) {
        Log.d("LIKE/FAIL", "$code $message")
    }

    override fun onDeleteLikeSuccess(code: Int, result: String) {
        Log.d("LIKEDELETE/SUCCESS", result)
    }

    override fun onDeleteLikeFailure(code: Int, message: String) {
        Log.d("LIKEDELETE/FAIL", "$code $message")
    }

}