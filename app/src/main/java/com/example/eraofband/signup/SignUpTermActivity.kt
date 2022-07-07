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
    private var allTrue = true //약관 모두동의가 되었는지 확인하기 위한 변수

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
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }


        setTextColor()
        binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)

        //모두동의 체크박스 연결
        binding.signupTermAllAgreeCb.setOnClickListener {
            if( binding.signupTermAllAgreeCb.isChecked) {
                //체크된 경우 모두 체크해주는 함수 호출
                allTrue()
                binding.signupTermNextBtn.isClickable = true
                binding.signupTermNextBtn.setBackgroundResource(R.drawable.blue_round_bg)
            } else {
                //체크되었다가 푼 경우 모두 해제해주는 함수 호출
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

    //allTrue 변수에 따라 버튼 색 & clickable 값 조정
    private fun changeButton() {
        binding.signupTermNextBtn.isClickable = allTrue
        if(allTrue){
            binding.signupTermNextBtn.setBackgroundResource(R.drawable.blue_round_bg)
        }else
            binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)
    }

    //다 체크되어있으면 모두 동의 버튼 변경 후, 버튼 변경 함수 호출
    private fun allCheck() {
        allTrue = binding.signupTermFirstCb.isChecked &&
                binding.signupTermSecondCb.isChecked &&
                binding.signupTermThirdCb.isChecked &&
                binding.signupTermForthCb.isChecked &&
                binding.signupTermFifthCb.isChecked

        binding.signupTermAllAgreeCb.isChecked = allTrue
        changeButton()
    }

    //체크박스 모두 해제 및 버튼 변경 함수 호출
    private fun allFalse() {
        binding.signupTermFirstCb.isChecked = false
        binding.signupTermSecondCb.isChecked = false
        binding.signupTermThirdCb.isChecked = false
        binding.signupTermForthCb.isChecked = false
        binding.signupTermFifthCb.isChecked = false
        allTrue = false
        changeButton()
    }

    //체크박스 모두 동의 및 버튼 변경 함수 호출
    private fun allTrue() {
        binding.signupTermFirstCb.isChecked = true
        binding.signupTermSecondCb.isChecked = true
        binding.signupTermThirdCb.isChecked = true
        binding.signupTermForthCb.isChecked = true
        binding.signupTermFifthCb.isChecked = true
        allTrue = true
        changeButton()
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