package com.example.eraofband.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupLocationBinding

class SignUpLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupLocationBinding
    private var user = User("", "", "", "", "", "", 0)
    private lateinit var location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = intent
        var user = intent.extras?.getSerializable("user") as User

        intent = Intent(this, SignUpSessionActivity::class.java)

        binding.signupLocationNextBtn.setOnClickListener {
            user.region = location
            intent.putExtra("user", user)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
        }

        binding.signupLocationBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        setColor()
        initSpinner()

    }

    private fun setColor() {  // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupLocationTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        val word = "활동 지역"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupLocationTitleTv.text = spannableString
    }

    private fun initSpinner() {  // 스피너 초기화
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.spinner_item, city)
        binding.signupLocationCitySp.adapter = cityAdapter
        binding.signupLocationCitySp.setSelection(0)

        // 도시 스피너 클릭 이벤트
        binding.signupLocationCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val area = resources.getStringArray(R.array.seoul)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                    binding.signupLocationAreaSp.adapter = areaAdapter
                    binding.signupLocationAreaSp.setSelection(0)

                    location = "서울 " + binding.signupLocationAreaSp.selectedItem.toString()
                }
                else {  // 경기도면 경기도 지역 연결
                    val area = resources.getStringArray(R.array.gyeonggido)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                    binding.signupLocationAreaSp.adapter = areaAdapter
                    binding.signupLocationAreaSp.setSelection(0)

                    location = "경기도 " + binding.signupLocationAreaSp.selectedItem.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val area = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                binding.signupLocationAreaSp.adapter = areaAdapter
                binding.signupLocationAreaSp.setSelection(0)
            }

        })

    }
}