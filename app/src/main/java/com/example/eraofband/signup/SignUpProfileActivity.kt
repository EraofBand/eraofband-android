package com.example.eraofband.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivitySignupProfileBinding

class SignUpProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupProfileNextBtn.setOnClickListener {
            startActivity(Intent(this@SignUpProfileActivity, SignUpLocationActivity::class.java))
        }

        binding.signupProfileBackIv.setOnClickListener {
            finish()
        }

        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupProfileTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        // 유저 이름 부분만 두껍게 표시
        val word = "프로필 사진"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupProfileTitleTv.text = spannableString

    }
}