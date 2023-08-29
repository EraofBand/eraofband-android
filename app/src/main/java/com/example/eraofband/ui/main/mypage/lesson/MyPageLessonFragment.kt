package com.example.eraofband.ui.main.mypage.lesson

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.Report
import com.example.eraofband.databinding.FragmentMypageLessonBinding
import com.example.eraofband.remote.user.getMyPage.GetMyPageResult
import com.example.eraofband.remote.user.getMyPage.GetMyPageService
import com.example.eraofband.remote.user.getMyPage.GetMyPageView
import com.example.eraofband.remote.user.getMyPage.GetUserLesson
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.ui.main.home.lesson.LessonInfoActivity

class MyPageLessonFragment : Fragment(), GetMyPageView, RefreshJwtView {
    private var _binding: FragmentMypageLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private val getMyPageLesson = GetMyPageService()
    private val refreshJwtService = RefreshJwtService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageLessonBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        getMyPageLesson.setUserView(this)
        refreshJwtService.setRefreshView(this)

        if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
            refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, getUserIdx())
        } else {
            getMyPageLesson.getMyInfo(getJwt()!!, getUserIdx())
        }
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    }

    private fun initRV(item: List<GetUserLesson>) {
        if(item.isEmpty()) {  // 비어있으면 비어있다고 메세지를 띄움
            binding.mypageNoLessonTv.visibility = View.VISIBLE
            binding.mypageLessonRv.visibility = View.GONE
            return
        }

        // 비어있지 않으면 리사이클러뷰 연결
        binding.mypageNoLessonTv.visibility = View.GONE
        binding.mypageLessonRv.visibility = View.VISIBLE

        val lessonRVAdapter = MyPageLessonRVAdapter(requireContext())
        binding.mypageLessonRv.adapter = lessonRVAdapter
        binding.mypageLessonRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        lessonRVAdapter.initLessonList(item)

        lessonRVAdapter.setMyItemClickListener(object : MyPageLessonRVAdapter.MyItemClickListener{
            override fun onShowDetail(lessonIdx: Int) {
                val intent = Intent(activity, LessonInfoActivity::class.java)
                intent.putExtra("lessonIdx", lessonIdx)

                startActivity(intent)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        Log.d("MYPAGE/SUC", "$result")

        initRV(result.getUserLesson)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPAGE/FAIL", "$code $message")
    }

    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")
        getMyPageLesson.getMyInfo(getJwt()!!, getUserIdx())
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REFRESH/FAIL", "$code $message")
    }
}