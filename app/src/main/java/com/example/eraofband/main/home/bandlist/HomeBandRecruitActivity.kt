package com.example.eraofband.main.home.bandlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityHomeBandRecruitBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeBandRecruitActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBandRecruitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBandRecruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandRecruitBackIv.setOnClickListener { finish() }  // 뒤로가기

        initViewPager()  // 뷰페이저 연결

    }

    private fun initViewPager() {
        val recruitVPAdapter = BandRecruitVPAdapter(this)
        binding.homeBandRecruitVp.adapter = recruitVPAdapter

        TabLayoutMediator(binding.homeBandRecruitTb, binding.homeBandRecruitVp) { tab, position ->
            when (position) {
                0 -> tab.text = "밴드 소개"
                1 -> tab.text = "세션 모집"
                2 -> tab.text = "앨범"
            }
        }.attach()
    }
}