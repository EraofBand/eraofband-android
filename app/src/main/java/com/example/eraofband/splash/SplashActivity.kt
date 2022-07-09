package com.example.eraofband.splash


import android.annotation.SuppressLint

import android.content.Context

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.eraofband.main.MainActivity
import com.example.eraofband.databinding.ActivitySplashBinding
import com.example.eraofband.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

import com.example.eraofband.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //실행여부 체크 위해 선언
        val prefs = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val isFinished = prefs.getBoolean("isFinished", false)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {  // 토큰이 없으면 블록 실행
                    if (isFinished) {  //실행 여부 체크 후 필요한 액티비티로 전환
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                        Log.d("onboard", "x")
                    } else {  // 토큰이 없고 최초 실행이 아니면 온보딩 액티비티 실행
                        startActivity(Intent(this, OnboardingActivity::class.java))
                        prefs.edit().putBoolean("isFinished", true).apply()
                        finish()
                        Log.d("onboard", "O")
                    }
                } else if (tokenInfo != null) {  // 토큰이 있으면
                    Log.d(
                        "tokenInfo", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }  /* 만약에 태스크에 호출하려는 엑티비티의 인스턴스가 이미 존재하고 있을 경우에
                 새로운 인스턴스를 생성하는 것 대신에 존재하고 있는 액티비티를 포그라운드로 가져온다.
                 그리고 호출한 인스턴스를 포그라운드로 가져올때까지 있었던 위의 인스턴스들을 모두 삭제한다. */
            }
        }, 1000)
    }
}