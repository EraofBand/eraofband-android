package com.example.eraofband.ui.signup

import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupNicknameBinding
import com.kakao.sdk.user.UserApiClient


class SignUpNicknameActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupNicknameBinding
    private var user = User("", "", "", "", "", "", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, SignUpGenderActivity::class.java)

        binding.signupNicknameNextBtn.setOnClickListener {
            if(binding.signupNicknameNicknameEt.text.isEmpty()) {
                setToast("닉네임을 입력해주세요!")
            }
            else {
                val nickname = binding.signupNicknameNicknameEt.text.toString()
                user.nickName = nickname
                intent.putExtra("user", user)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
            }
        }
        nickNameTW()

        binding.signupNicknameBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("kakaoinfo", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                binding.signupNicknameNameTv.text = "${user.kakaoAccount?.profile?.nickname}"
            }
        }

        //setTextColor()
    }

    /*private fun setTextColor() {
        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupNicknameNameTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        // 유저 이름 부분만 두껍게 표시
        val word = "이승희"
        val start = nickname.indexOf(nickname)
        val end = nickname.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupNicknameNameTv.text = spannableString
    }*/

    private fun nickNameTW() {
        binding.signupNicknameNicknameEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 7){
                    setToast("닉네임은 8글자 이하입니다!")
                }
            }

        })
    }
    private fun setToast(str : String) {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = str

        val display = windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기
        val height = size.y / 2  // 토스트 메세지가 중간에 고정되어있기 때문에 높이 / 2

        // 중간부터 marginBottom, 버튼 높이 / 2 만큼 빼줌
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 80.toPx() - binding.signupNicknameNextBtn.height / 2)
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}

