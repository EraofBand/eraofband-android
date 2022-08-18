package com.example.eraofband.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivitySignupFinishBinding
import com.example.eraofband.ui.main.MainActivity
import com.kakao.sdk.user.UserApiClient

class SignUpFinishActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("kakaoinfo", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                binding.signupFinishNameTv.text = user.kakaoAccount!!.profile!!.nickname!!
            }
        }

        binding.signupFinishNextBtn.setOnClickListener {
            intent = Intent(this@SignUpFinishActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finishAffinity()}
    }
}