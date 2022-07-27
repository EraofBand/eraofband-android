package com.example.eraofband.main.home.session.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eraofband.databinding.ActivityBandRecruitBinding
import com.example.eraofband.remote.getBand.GetBandResult
import com.example.eraofband.remote.getBand.GetBandService
import com.example.eraofband.remote.getBand.GetBandView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class BandRecruitActivity: AppCompatActivity(), GetBandView {

    private lateinit var binding: ActivityBandRecruitBinding

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandRecruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandRecruitBackIv.setOnClickListener { finish() }  // 뒤로가기

        binding.homeBandRecruitListIv.setOnClickListener {
            startActivity(Intent(this, BandEditActivity::class.java)) // 밴드 수정 이동
        }
    }

    override fun onResume() {
        super.onResume()

        val bandService = GetBandService()
        bandService.setBandView(this)
        bandService.getBand(getJwt()!!, intent.getIntExtra("bandIdx", 0))
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
        // 밴드 정보 연동
        binding.homeBandRecruitBandTitleTv.text = result.bandTitle  // 밴드 이름 연동

        Glide.with(this).load(result.bandImgUrl).into(binding.homeBandRecruitBandImgIv)  // 밴드 이미지 연동
        binding.homeBandRecruitBandImgIv.clipToOutline = true  // 모서리 깎기

        binding.homeBandRecruitBandNameTv.text = result.bandTitle  // 밴드 이름 연동
        binding.homeBandRecruitBandIntroTv.text = result.bandIntroduction  // 한줄 소개 연동
        binding.homeBandRecruitCntTv.text = "${result.memberCount} / ${result.capacity}"  // 멤버 수

        // viewPager로 데이터를 넘기기 위해 저장
        val bandSP = getSharedPreferences("band", MODE_PRIVATE)
        val bandEdit = bandSP.edit()

        val bandJson = gson.toJson(result)
        bandEdit.putString("bandInfo", bandJson)
        bandEdit.apply()

        initViewPager()  // 뷰페이저 연결
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETBAND/FAIL", "$code $message")
    }
}