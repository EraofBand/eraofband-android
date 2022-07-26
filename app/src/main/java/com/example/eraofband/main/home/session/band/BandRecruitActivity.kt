package com.example.eraofband.main.home.session.band

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityBandRecruitBinding
import com.google.android.material.tabs.TabLayoutMediator

class BandRecruitActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBandRecruitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandRecruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandRecruitBackIv.setOnClickListener { finish() }  // 뒤로가기
        binding.homeBandRecruitListIv.setOnClickListener {
            startActivity(Intent(this, BandEditActivity::class.java)) // 밴드 수정 이동
        }
        initViewPager()  // 뷰페이저 연결

    }

    private fun initViewPager() {
        val bandRecruitVPAdapter = BandRecruitVPAdapter(this)
        binding.homeBandRecruitVp.adapter = bandRecruitVPAdapter

        TabLayoutMediator(binding.homeBandRecruitTb, binding.homeBandRecruitVp) { tab, position ->
            when (position) {
                0 -> tab.text = "밴드 소개"
                1 -> tab.text = "세션 모집"
                2 -> tab.text = "앨범"
            }
        }.attach()
    }
}