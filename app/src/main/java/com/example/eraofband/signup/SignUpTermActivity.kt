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
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupTermBinding
import com.example.eraofband.main.MainActivity
import com.example.eraofband.remote.kakaologin.KakaoLoginService
import com.example.eraofband.remote.kakaologin.KakaoLoginView
import com.example.eraofband.remote.kakaologin.LoginResult

class SignUpTermActivity : AppCompatActivity(), KakaoLoginView {

    private lateinit var binding: ActivitySignupTermBinding
    private var user = User("", "", "", "", "", "", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = intent
        user = intent.extras?.getSerializable("user") as User
        Log.d("user-do", user.toString())

        binding.signupTermNextBtn.setOnClickListener {

            val tokenSP = getSharedPreferences("token", MODE_PRIVATE)

            val loginService = KakaoLoginService()

            loginService.setLoginView(this)
            loginService.login(user, tokenSP.getString("tokenInfo", ""))
            intent = Intent(this@SignUpTermActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finishAffinity()
        }

        binding.signupTermBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        // 기본적으로 글씨 색 설정, 다음으로 넘어가지 못하도록 설정
        setTextColor()
        binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)
        binding.signupTermNextBtn.isClickable = false

        checkBoxListener()

    }

    private fun checkBoxListener() {  // 체크박스 리스너
        binding.signupTermAllAgreeCb.setOnClickListener {
            // allAgree를 밑에 안넣은 이유는... 클릭 여부에 따라서 전체 동의를 변경해야하기 때문입니닷
            if(binding.signupTermAllAgreeCb.isChecked) {  // 모두 true로 변경
                allTrue()
            } else {  // 모두 false로 변경
                allFalse()
            }
        }

        val checkListener = CompoundButton.OnCheckedChangeListener { checkbox, _ ->
            // 각 체크박스를 누를 때마다 전체동의 여부와 필수동의 여부를 확인
            when (checkbox.id) {
                R.id.signup_term_first_cb -> {
                    allCheck()
                    essentialCheck()
                }
                R.id.signup_term_second_cb -> {
                    allCheck()
                    essentialCheck()
                }
                R.id.signup_term_third_cb -> {
                    allCheck()
                    essentialCheck()
                }
                R.id.signup_term_forth_cb -> {
                    allCheck()
                    essentialCheck()
                }
                R.id.signup_term_fifth_cb -> {
                    allCheck()
                    essentialCheck()
                }
            }
        }

        binding.signupTermFirstCb.setOnCheckedChangeListener(checkListener)
        binding.signupTermSecondCb.setOnCheckedChangeListener(checkListener)
        binding.signupTermThirdCb.setOnCheckedChangeListener(checkListener)
        binding.signupTermForthCb.setOnCheckedChangeListener(checkListener)
        binding.signupTermFifthCb.setOnCheckedChangeListener(checkListener)
    }

    //allTrue 변수에 따라 버튼 색 & clickable 값 조정
    private fun changeButton(allTrue : Boolean) {
        binding.signupTermNextBtn.isClickable = allTrue
        if(allTrue){
            binding.signupTermNextBtn.setBackgroundResource(R.drawable.blue_round_bg)
        } else {
            binding.signupTermNextBtn.setBackgroundResource(R.drawable.gray_round_bg)
        }
    }

    private fun allCheck() {  // 전체동의 여부 확인 후 상태 지정
        val allTrue = binding.signupTermFirstCb.isChecked && binding.signupTermSecondCb.isChecked &&
                binding.signupTermThirdCb.isChecked && binding.signupTermForthCb.isChecked && binding.signupTermFifthCb.isChecked

        binding.signupTermAllAgreeCb.isChecked = allTrue
        changeButton(allTrue)
    }


    private fun essentialCheck() {  // 필수동의 여부와 버튼 상태 변경
        val essential = binding.signupTermFirstCb.isChecked && binding.signupTermSecondCb.isChecked

        if(essential) {
            binding.signupTermNextBtn.setBackgroundResource(R.drawable.blue_round_bg)
            binding.signupTermNextBtn.isClickable = essential
        }
    }

    private fun allFalse() {  // 전체동의 취소
        binding.signupTermFirstCb.isChecked = false
        binding.signupTermSecondCb.isChecked = false
        binding.signupTermThirdCb.isChecked = false
        binding.signupTermForthCb.isChecked = false
        binding.signupTermFifthCb.isChecked = false

        allCheck()  // 전체동의 상태 지정
    }

    private fun allTrue() {  // 전체동의 설정
        binding.signupTermFirstCb.isChecked = true
        binding.signupTermSecondCb.isChecked = true
        binding.signupTermThirdCb.isChecked = true
        binding.signupTermForthCb.isChecked = true
        binding.signupTermFifthCb.isChecked = true

        allCheck()  // 전체동의 상태 지정
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
            1000 -> {  // 로그인 완료
                Log.d("CHECK-SUCCESS", result.toString())
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        Log.d("CHECK-FAILURE", "$code $message")
    }
}