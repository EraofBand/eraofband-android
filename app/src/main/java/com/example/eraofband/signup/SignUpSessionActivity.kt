package com.example.eraofband.signup

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupSessionBinding

class SignUpSessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupSessionBinding
    private var user = User("", "", "", "", "", "", 0)
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
            if(session == -1) {
                setToast()
            } else {
                user.session = session
                intent.putExtra("user", user)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
            }
        }

        binding.signupSessionBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        setTextColor()

        binding.signupSessionVocalCb.setOnClickListener {
            session = 0
            binding.signupSessionVocalCb.isChecked = true
            binding.signupSessionGuitarCb.isChecked = false
            binding.signupSessionBaseCb.isChecked = false
            binding.signupSessionKeyboardCb.isChecked = false
            binding.signupSessionDrumCb.isChecked = false
        }

        binding.signupSessionGuitarCb.setOnClickListener {
            session = 1
            binding.signupSessionGuitarCb.isChecked = true
            binding.signupSessionVocalCb.isChecked = false
            binding.signupSessionBaseCb.isChecked = false
            binding.signupSessionKeyboardCb.isChecked = false
            binding.signupSessionDrumCb.isChecked = false
        }

        binding.signupSessionBaseCb.setOnClickListener {
            session = 2
            binding.signupSessionBaseCb.isChecked = true
            binding.signupSessionGuitarCb.isChecked = false
            binding.signupSessionVocalCb.isChecked = false
            binding.signupSessionKeyboardCb.isChecked = false
            binding.signupSessionDrumCb.isChecked = false
        }

        binding.signupSessionKeyboardCb.setOnClickListener {
            session = 3
            binding.signupSessionKeyboardCb.isChecked = true
            binding.signupSessionGuitarCb.isChecked = false
            binding.signupSessionBaseCb.isChecked = false
            binding.signupSessionVocalCb.isChecked = false
            binding.signupSessionDrumCb.isChecked = false
        }

        binding.signupSessionDrumCb.setOnClickListener {
            session = 4
            binding.signupSessionDrumCb.isChecked = true
            binding.signupSessionGuitarCb.isChecked = false
            binding.signupSessionBaseCb.isChecked = false
            binding.signupSessionKeyboardCb.isChecked = false
            binding.signupSessionVocalCb.isChecked = false
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

    private fun setToast() {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = "세션을 선택해주세요!"

        val display = windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기
        val height = size.y / 2  // 토스트 메세지가 중간에 고정되어있기 때문에 높이 / 2

        // 중간부터 marginBottom, 버튼 높이 / 2 만큼 빼줌
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 80.toPx() - binding.signupSessionNextBtn.height / 2)
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}