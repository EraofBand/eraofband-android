package com.example.eraofband.ui.main.home.lesson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ActivityLessonInfoBinding
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoResult
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoService
import com.example.eraofband.remote.lesson.getLessonInfo.GetLessonInfoView
import com.example.eraofband.remote.lesson.getLessonInfo.LessonMembers
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListResult
import com.example.eraofband.remote.lessonLike.LessonLikeResult
import com.example.eraofband.remote.lessonLike.LessonLikeService
import com.example.eraofband.remote.lessonLike.LessonLikeView
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeResult
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeService
import com.example.eraofband.remote.lesson.lessonLike.LessonLikeView
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity


class LessonInfoActivity() : AppCompatActivity(), GetLessonInfoView, LessonLikeView {
    private lateinit var binding: ActivityLessonInfoBinding
    private lateinit var lessonStudentRVAdapter: LessonStudentRVAdapter
    private var lessonIdx: Int? = null
    private var teacherIdx: Int? = null
    private var isFull = false
    private var studentIdxList = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lessonLike = LessonLikeService() // 레슨 좋아요 api
        lessonLike.setLikeView(this)

        binding.lessonInfoBackIv.setOnClickListener { // 뒤로가기
            finish()
        }
        binding.lessonInfoListIv.setOnClickListener {  // 레슨 수정 클릭리스너
            val intent = Intent(this, LessonEditActivity()::class.java)
            intent.putExtra("lessonIdx", lessonIdx)
            startActivity(intent)
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
    }

    override fun onResume() {
        super.onResume()
        lessonIdx = intent.getIntExtra("lessonIdx", 0)
        var getLessonInfo = GetLessonInfoService()
        getLessonInfo.getLessonInfoView(this)
        getLessonInfo.getLessonInfo(getJwt()!!, lessonIdx!!)
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
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
    }

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