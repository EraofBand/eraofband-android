package com.example.eraofband.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뷰페이저 어뎁터 연결
        val fragmentList = listOf(OnBandFragment(), OnLessonFragment(), OnPortfolioFragment(), OnTogetherFragment())
        val onboardingAdapter = OnboardingVPAdapter(this)
        onboardingAdapter.fragments = fragmentList
        binding.onboardingVp.adapter = onboardingAdapter
    }
}