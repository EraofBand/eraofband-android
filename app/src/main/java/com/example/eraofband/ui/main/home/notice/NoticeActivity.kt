package com.example.eraofband.ui.main.home.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityNoticeBinding
import com.example.eraofband.remote.getLessonLikeList.GetLessonLikeListService
import com.example.eraofband.remote.notice.GetNoticeResponse
import com.example.eraofband.remote.notice.GetNoticeResult
import com.example.eraofband.remote.notice.GetNoticeService
import com.example.eraofband.remote.notice.GetNoticeView
import com.example.eraofband.ui.main.home.lessonlike.HomeLessonLikeRVAdapter

class NoticeActivity : AppCompatActivity(), GetNoticeView {

    private lateinit var binding : ActivityNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val noticeService = GetNoticeService()
        noticeService.setNoticeView(this)
        noticeService.getNotice(getJwt()!!, getUserIdx())
    }

    private fun initNoticeRV(list: List<GetNoticeResult>) {
        val noticeRVAdapter = NoticeRVAdapter(list)
        binding.noticeRv.adapter = noticeRVAdapter
        binding.noticeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: List<GetNoticeResult>) {
        Log.d("GET/SUCCESS", "$result")
        initNoticeRV(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET/Failure", "$code $message")
    }
}