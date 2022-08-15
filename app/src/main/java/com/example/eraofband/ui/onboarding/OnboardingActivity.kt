package com.example.eraofband.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityOnboardingBinding
import com.example.eraofband.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뷰페이저 어뎁터 연결
        val fragmentList = listOf(OnBandFragment(), OnLessonFragment(), OnPortfolioFragment(), OnTogetherFragment())
        val onboardingAdapter = OnboardingVPAdapter(this)
        onboardingAdapter.fragments = fragmentList

        binding.onboardingVp.adapter = onboardingAdapter
        binding.onboardingIndicator.attachTo(binding.onboardingVp)

        //스킵 버튼 누르면 로그인 액티비티 이동
        binding.onboardingSkipTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}