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
import com.example.eraofband.databinding.ActivitySignupTermBinding
import com.example.eraofband.main.MainActivity
import com.example.eraofband.remote.KakaoLoginService
import com.example.eraofband.remote.KakaoLoginView
import com.example.eraofband.remote.LoginResult

class SignUpTermActivity : AppCompatActivity(), KakaoLoginView {

    private lateinit var binding: ActivitySignupTermBinding
    private var allTrue = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupTermNextBtn.setOnClickListener {

            val tokenSP = getSharedPreferences("token", MODE_PRIVATE)

            val loginService = KakaoLoginService()

            loginService.setLoginView(this)
            loginService.login(User("0121", "여", "보리", "what", "관악구", 0), tokenSP.getString("tokenInfo", ""))

            val intent = Intent(this@SignUpTermActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finishAffinity()
        }



        binding.signupTermBackIv.setOnClickListener {
            finish()
        }


        setTextColor()
        binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)

        binding.signupTermAllAgreeCb.setOnClickListener {
            if( binding.signupTermAllAgreeCb.isChecked) {
                allTrue()
                binding.signupTermNextBtn.isClickable = true
                binding.signupTermNextBtn.setBackgroundResource(R.drawable.blue_round_bg)
            } else {
                allFalse()
                binding.signupTermNextBtn.isClickable = false
                binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)
            }
        }

        binding.signupTermFirstCb.setOnClickListener {
            binding.signupTermFirstCb.isChecked
            allCheck()
        }

        binding.signupTermSecondCb.setOnClickListener {
            binding.signupTermSecondCb.isChecked
            allCheck()
        }

        binding.signupTermThirdCb.setOnClickListener {
            binding.signupTermThirdCb.isChecked
            allCheck()
        }

        binding.signupTermForthCb.setOnClickListener {
            binding.signupTermForthCb.isChecked
            allCheck()
        }

        binding.signupTermFifthCb.setOnClickListener {
            binding.signupTermFirstCb.isChecked
            allCheck()
        }

    }

    private fun allCheck() {
        allTrue = binding.signupTermFirstCb.isChecked &&
                binding.signupTermSecondCb.isChecked &&
                binding.signupTermThirdCb.isChecked &&
                binding.signupTermForthCb.isChecked &&
                binding.signupTermFifthCb.isChecked

        binding.signupTermAllAgreeCb.isChecked = allTrue
    }

    private fun allFalse() {
        binding.signupTermFirstCb.isChecked = false
        binding.signupTermSecondCb.isChecked = false
        binding.signupTermThirdCb.isChecked = false
        binding.signupTermForthCb.isChecked = false
        binding.signupTermFifthCb.isChecked = false
        allTrue = false
    }

    private fun allTrue() {
        binding.signupTermFirstCb.isChecked = true
        binding.signupTermSecondCb.isChecked = true
        binding.signupTermThirdCb.isChecked = true
        binding.signupTermForthCb.isChecked = true
        binding.signupTermFifthCb.isChecked = true
        allTrue = allTrue

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

    override fun onLoginSuccess(code: Int, result: LoginResult) {
        when(code) {
            200 -> {  // 로그인 완료
                Log.d("SUCCESS", result.toString())
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        Log.d("CHECK-FAILURE", "$code $message")
    }
}