package com.example.eraofband.main.home.lesson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.data.Student
import com.example.eraofband.databinding.ActivityLessonInfoBinding
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoResult
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoService
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoView
import com.example.eraofband.remote.getLessonInfo.LessonMembers

class LessonInfoActivity(): AppCompatActivity(), GetLessonInfoView {

    private lateinit var binding: ActivityLessonInfoBinding
    private lateinit var lessonStudentRVAdapter: LessonStudentRVAdapter
    private var lessonIdx: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lessonIdx = intent.getIntExtra("lessonIdx", 0)

        binding.lessonInfoBackIv.setOnClickListener { finish() }  // 뒤로가기
        binding.lessonInfoListIv.setOnClickListener {  // 레슨 수정 클릭리스너
            var intent = Intent(this, LessonEditActivity()::class.java)
            intent.putExtra("lessonIdx", lessonIdx)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        var getLessonInfo = GetLessonInfoService()
        getLessonInfo.getLessonInfoView(this)
        getLessonInfo.getLessonInfo(getJwt()!!, lessonIdx!!)
    }
    private fun initRecyclerView(studentList : List<LessonMembers>) {
        // 수강생 목록 리사이클러뷰
        lessonStudentRVAdapter = LessonStudentRVAdapter()
        binding.lessonInfoStudentRv.adapter = lessonStudentRVAdapter
        binding.lessonInfoStudentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lessonStudentRVAdapter.initStudentList(studentList)

    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetLessonInfoSuccess(code: Int, result: GetLessonInfoResult) { // 여기에 initRecyclerView
        Log.d("GET/SUCCESS", result.toString())

        Glide.with(this)  // 레슨 이미지
            .load(result.lessonImgUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.lessonInfoLessonImgIv)
        binding.lessonInfoLessonImgIv.clipToOutline = true  // 모서리 깎기

        binding.lessonInfoTitleTv.text = result.lessonTitle // 상단바 타이틀
        binding.lessonInfoLessonTitleTv.text = result.lessonTitle // 타이틀
        binding.lessonInfoLessonIntroTv.text = result.lessonIntroduction // 한 줄 소개
        binding.lessonInfoCntTv.text = "${result.memberCount} / ${result.capacity}명" // 인원 수
        binding.lessonInfoSessionTv.text = when(result.lessonSession) {  // 레슨 종목
            0 -> "보컬"
            1 -> "기타"
            2 -> "베이스"
            3 -> "키보드"
            else -> "드럼"
        }
        binding.lessonInfoAreaTv.text = result.lessonRegion // 레슨 지역
        Glide.with(this)  // 강사 이미지
            .load(result.profileImgUrl)
            .apply(RequestOptions.centerCropTransform())
            .apply(RequestOptions.circleCropTransform())
            .into(binding.lessonInfoTeacherProfileIv)

        binding.lessonInfoTeacherNicknameTv.text = result.nickName // 강사 닉네임
        binding.lessonInfoTeacherIntroTv.text = result.userIntroduction  // 강사 한 줄 소개
        binding.lessonInfoIntroTv.text = result.lessonContent // 레슨 소개
        binding.lessonInfoChatTv.text = result.chatRoomLink // 채팅 링크

        initRecyclerView(result.lessonMembers) // 수강생 목록

    }
    override fun onGetLessonInfoFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }
}