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
import com.example.eraofband.databinding.ActivitySignupLocationBinding
import com.example.eraofband.databinding.ActivitySignupSessionBinding

class SignUpSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupSessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSessionNextBtn.setOnClickListener {
            startActivity(Intent(this@SignUpSessionActivity, SignUpTermActivity::class.java))
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
        }

        binding.signupSessionBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }
    }

    private fun setTextColor() {
        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupSessionTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        val word = "희망하는 세션"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupSessionTitleTv.text = spannableString

    }
}