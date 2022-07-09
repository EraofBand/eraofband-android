package com.example.eraofband.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupSessionBinding

class SignUpSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupSessionBinding
    private var user = User("", "", "", "", "", 0)
    private var session = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = intent
        user = intent.extras?.getSerializable("user") as User

        intent = Intent(this, SignUpTermActivity::class.java)

        binding.signupSessionNextBtn.setOnClickListener {
            allFalse()
            if(session == -1) binding.signupSessionNextBtn.isClickable = false
            binding.signupSessionNextBtn.isClickable

            user.session = session
            intent.putExtra("user", user)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
        }

        binding.signupSessionBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        setTextColor()

        binding.signupSessionVocalCb.setOnClickListener {
            session = 0
        }

        binding.signupSessionGuitarCb.setOnClickListener {
            session = 1
        }

        binding.signupSessionBaseCb.setOnClickListener {
            session = 2
        }

        binding.signupSessionKeyboardCb.setOnClickListener {
            session = 3
        }

        binding.signupSessionDrumCb.setOnClickListener {
            session = 4
        }
    }

    private fun allFalse() {
        if(!binding.signupSessionVocalCb.isChecked &&
            !binding.signupSessionGuitarCb.isChecked &&
            !binding.signupSessionBaseCb.isChecked &&
            !binding.signupSessionKeyboardCb.isChecked &&
            !binding.signupSessionDrumCb.isChecked )
            session = -1
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