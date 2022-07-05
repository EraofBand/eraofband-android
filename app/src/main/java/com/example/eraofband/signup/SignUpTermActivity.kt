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
import com.example.eraofband.MainActivity
import com.example.eraofband.databinding.ActivitySignupTermBinding

class SignUpTermActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupTermBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupTermNextBtn.setOnClickListener {
            val intent = Intent(this@SignUpTermActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finishAffinity()
        }

        binding.signupTermBackIv.setOnClickListener {
            finish()
        }

        setTextColor()

    }

    private fun setTextColor() {
        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupTermTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        val word = "동의"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupTermTitleTv.text = spannableString

    }
}