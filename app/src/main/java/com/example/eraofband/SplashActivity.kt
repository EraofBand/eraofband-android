package com.example.eraofband


import android.annotation.SuppressLint

import android.content.Context

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.eraofband.databinding.ActivitySplashBinding

import com.example.eraofband.onboarding.OnboardingActivity
import java.lang.Boolean.getBoolean

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({

            //실행 여부 체크 후 필요한 액티비티로 전환
            if(isOnboardingFinished()){
                startActivity(Intent(this, LoginActivity::class.java))
            } else{
                startActivity(Intent(this, OnboardingActivity::class.java))
            }


            finish()
        }, 1000)
    }

    //온보딩 액티비티 실행 됐었는지 체크하는 함수
    private fun isOnboardingFinished(): Boolean {
        val prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        return prefs.getBoolean("finished", false)
    }

}