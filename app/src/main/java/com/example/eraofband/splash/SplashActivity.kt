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
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError

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
            if (AuthApiClient.instance.hasToken()) {  // 발급받은 토급이 카카오 sdk 내부에 존재하는가?
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {  // 토큰은 있지만 에러발생, 재로그인 필요
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                            Log.d("kakaoLogin", error.toString())
                        } else {  // 기타 에러, 재로그인 필요
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                            Log.d("kakaoLogin", error.toString())
                        }
                    } else {  // 토큰 유효성 체크 성공(필요 시 토큰 갱신)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        if (tokenInfo != null) {
                            Log.d(
                                "kakaoLogin", "자동 로그인 완" +
                                        "\n회원번호: ${tokenInfo.id}" +
                                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                        }
                    }
                }
            } else {  // 토큰X 로그인 필요
                if (isFinished) {  // 최초 실행이 아닌 경우 로그인 화면 전환
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {  // 최초 실행인 경우 온보딩 화면 전환
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    prefs.edit().putBoolean("isFinished", true).apply()
                    finish()
                }
            }
        }, 1000)
    }
}