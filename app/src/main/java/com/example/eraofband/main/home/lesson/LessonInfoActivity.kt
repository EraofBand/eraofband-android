package com.example.eraofband.main.home.lesson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.data.Student
import com.example.eraofband.databinding.ActivityLessonInfoBinding
import com.example.eraofband.main.home.session.band.BandEditActivity
import com.example.eraofband.remote.deleteBand.DeleteBandService
import com.example.eraofband.remote.deleteLesson.DeleteLessonResponse
import com.example.eraofband.remote.deleteLesson.DeleteLessonService
import com.example.eraofband.remote.deleteLesson.DeleteLessonView
import com.example.eraofband.remote.deleteUserLesson.DeleteUserLessonResponse
import com.example.eraofband.remote.deleteUserLesson.DeleteUserLessonService
import com.example.eraofband.remote.deleteUserLesson.DeleteUserLessonView
import com.example.eraofband.remote.getBand.SessionMembers
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoResult
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoService
import com.example.eraofband.remote.getLessonInfo.GetLessonInfoView
import com.example.eraofband.remote.getLessonInfo.LessonMembers

class LessonInfoActivity(): AppCompatActivity(), GetLessonInfoView, DeleteLessonView, DeleteUserLessonView {

    private lateinit var binding: ActivityLessonInfoBinding
    private lateinit var lessonStudentRVAdapter: LessonStudentRVAdapter
    private var lessonIdx = 0

    private var leaderIdx = 0
    private var lessonMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lessonIdx = intent.getIntExtra("lessonIdx", 0)

        binding.lessonInfoBackIv.setOnClickListener { finish() }  // 뒤로가기

        binding.lessonInfoListIv.setOnClickListener {
            showPopup(binding.lessonInfoListIv)
        }
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun showPopup(view: View) {
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.lesson_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.lesson_edit) {
                val intent = Intent(this, LessonEditActivity::class.java)
                intent.putExtra("lessonIdx", lessonIdx)
                startActivity(intent)
            } else if(item!!.itemId == R.id.lesson_delete){
                //레슨 삭제
                val deleteLessonService = DeleteLessonService()
                deleteLessonService.setDeleteView(this)
                deleteLessonService.deleteLesson(getJwt()!!, lessonIdx, getUserIdx())
            } else{
                val deleteUserLessonService = DeleteUserLessonService()
                deleteUserLessonService.setDeleteView(this)
                deleteUserLessonService.deleteUserLesson(getJwt()!!, lessonIdx)
            }
            false
        }

        if(getUserIdx() == leaderIdx){
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

        leaderIdx = result.userIdx //레슨 리더

        lessonMember = checkUserIdx(result.lessonMembers)

        initRecyclerView(result.lessonMembers) // 수강생 목록

    }

    private fun checkUserIdx(memberList: List<LessonMembers>): Boolean {
        // 만약 내 userIdx가 멤버 리스트의 userIdx와 같으면 밴드 멤버에 속함
        if(memberList.isEmpty()) return false  // 밴드 멤버가 없으면 무조건 false

        for(element in memberList) {
            if(getUserIdx() == element.userIdx) return true
        }

        return false
    }

    override fun onGetLessonInfoFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

    override fun onDeleteSuccess(code: Int, result: String) {
        Log.d("DELETE BAND / SUCCESS", result)
    }

    override fun onDeleteFailure(response: DeleteLessonResponse) {
        Log.d("DELETE BAND / FAIL", response.toString())
    }

    override fun onDeleteUserSuccess(code: Int, result: String) {
        Log.d("DELETE BAND / SUCCESS", result)
    }

    override fun onDeleteUserFailure(response: DeleteUserLessonResponse) {
        Log.d("DELETE BAND / FAIL", response.toString())
    }
}