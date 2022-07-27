package com.example.eraofband.main.home.lesson

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ActivityLessonInfoBinding

class LessonInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLessonInfoBinding

    private lateinit var lessonStudentRVAdapter: LessonStudentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lessonInfoBackIv.setOnClickListener { finish() }  // 뒤로가기
        binding.lessonInfoListIv.setOnClickListener {
            startActivity(Intent(this, LessonEditActivity::class.java))
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // 수강생 목록 리사이클러뷰
        lessonStudentRVAdapter = LessonStudentRVAdapter()
        binding.lessonInfoStudentRv.adapter = lessonStudentRVAdapter
        binding.lessonInfoStudentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
/*
        val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", "")
        )

        lessonStudentRVAdapter.initStudentList(bandList)*/

    }
}