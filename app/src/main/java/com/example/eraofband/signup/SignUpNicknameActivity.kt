package com.example.eraofband.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivitySignupNicknameBinding

class SignUpNicknameActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupNicknameNextBtn.setOnClickListener {
            startActivity(Intent(this@SignUpNicknameActivity, SignUpGenderActivity::class.java))
        }

        binding.signupNicknameBackIv.setOnClickListener {
            finish()
        }

        setTextColor()
    }

    private fun setTextColor() {
        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupNicknameNameTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        // 유저 이름 부분만 두껍게 표시
        val word = "이승희"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupNicknameNameTv.text = spannableString
    }
}