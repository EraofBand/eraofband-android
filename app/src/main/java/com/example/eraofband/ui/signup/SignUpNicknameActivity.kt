package com.example.eraofband.ui.signup

import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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

        binding.signupNicknameNextBtn.setOnClickListener {
            val nickname = "${binding.signupNicknameNicknameEt.text.trim()}"
            user.nickName = nickname

            if(nickname.isEmpty()) {
                setToast("닉네임을 입력해주세요!")
            }
            else {
                val intent = Intent(this, SignUpGenderActivity::class.java)
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

    }

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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
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

