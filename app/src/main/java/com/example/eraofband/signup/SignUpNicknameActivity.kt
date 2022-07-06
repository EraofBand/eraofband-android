package com.example.eraofband.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
            if(binding.signupNicknameNicknameEt.text.isEmpty()) {
                setToast()
            }
            else {
                startActivity(Intent(this@SignUpNicknameActivity, SignUpGenderActivity::class.java))
            }
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

    private fun setToast() {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = "닉네임을 입력해주세요!"



        toast.view = view
        toast.setMargin(250F, 0F)
        toast.setGravity(Gravity.FILL_HORIZONTAL,0, 720)
        toast.show()
    }
}