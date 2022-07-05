package com.example.eraofband

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.eraofband.databinding.ActivitySplashBinding
import com.example.eraofband.onboarding.OnboardingActivity
import java.lang.Boolean.getBoolean

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        //실행여부 체크 위해 선언
        val prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        var isFinished = prefs.getBoolean("isFinished", false)

        handler.postDelayed({
            //실행 여부 체크 후 필요한 액티비티로 전환
            if(isFinished){
                startActivity(Intent(this, LoginActivity::class.java))
            } else{
                startActivity(Intent(this, OnboardingActivity::class.java))
                prefs.edit().putBoolean("isFinished", true).apply()
            }

            finish()
        }, 1000)
    }
}