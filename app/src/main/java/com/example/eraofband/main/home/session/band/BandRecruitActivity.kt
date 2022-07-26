package com.example.eraofband.main.home.session.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityBandRecruitBinding
import com.example.eraofband.remote.getBand.GetBandResult
import com.example.eraofband.remote.getBand.GetBandService
import com.example.eraofband.remote.getBand.GetBandView
import com.google.android.material.tabs.TabLayoutMediator

class BandRecruitActivity: AppCompatActivity(), GetBandView {

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

        val bandService = GetBandService()
        bandService.setBandView(this)
        bandService.getBand(getJwt()!!, 5)

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

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: GetBandResult) {
        Log.d("GETBAND/SUC", result.toString())
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETBAND/FAIL", "$code $message")
    }
}