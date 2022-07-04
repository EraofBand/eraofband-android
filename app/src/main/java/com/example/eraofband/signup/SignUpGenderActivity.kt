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
import com.example.eraofband.databinding.ActivitySignupGenderBinding

class SignUpGenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupGenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupGenderNextBtn.setOnClickListener {
            startActivity(Intent(this@SignUpGenderActivity, SignUpProfileActivity::class.java))
        }

        binding.signupGenderBackIv.setOnClickListener {
            finish()
        }


        val text = binding.signupGenderTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(text)  //객체 생성

        // 성별, 생년월일만 파랗고 두껍게 표시
        val word1 = "성별"
        val start1 = text.indexOf(word1)
        val end1 = start1 + word1.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupGenderTitleTv.text = spannableString

        val text2 = binding.signupGenderTitleTv.text  // 텍스트 가져옴
        val spannableString2 = SpannableString(text2)  //객체 생성

        val word2 = "생년월일"
        val start2 = text.indexOf(word2)
        val end2 = start2 + word2.length

        spannableString2.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString2.setSpan(StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupGenderTitleTv.text = spannableString2

    }
}