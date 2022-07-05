package com.example.eraofband.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.eraofband.LoginActivity
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

        //뷰페이저 인디케이터 연결
        binding.onboardingVp.registerOnPageChangeCallback(object :
            OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.onboardingIndicator1Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_nonselected1))
                binding.onboardingIndicator2Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_nonselected2))
                binding.onboardingIndicator3Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_nonselected2))
                binding.onboardingIndicator4Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_nonselected2))

                //뷰페이저 넘길 때마다 컬러 및 크기변경 적용
                when(position){
                    0 -> binding.onboardingIndicator1Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_selected))
                    1 -> binding.onboardingIndicator2Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_selected))
                    2 -> binding.onboardingIndicator3Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_selected))
                    3 -> binding.onboardingIndicator4Iv.setImageDrawable(getDrawable(R.drawable.indicator_circle_selected))
                }
            }
        })

        //스킵 버튼 누르면 로그인 액티비티 이동
        binding.onboardingSkipTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}