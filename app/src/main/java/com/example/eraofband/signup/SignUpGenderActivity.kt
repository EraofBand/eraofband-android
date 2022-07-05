package com.example.eraofband.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivitySignupGenderBinding
import java.text.SimpleDateFormat
import java.util.*


class SignUpGenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupGenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupGenderNextBtn.setOnClickListener {
            startActivity(Intent(this@SignUpGenderActivity, SignUpProfileActivity::class.java))
        }

        binding.signupGenderBackIv.setOnClickListener {
            finish()
        }

        setTextColor()
        binding.signupGenderRealBirthdayTv.text = setDate()

        binding.signupGenderRealBirthdayTv.setOnClickListener {
            val dateDialog = DialogDatePicker()
            dateDialog.show(supportFragmentManager, "dateDialog")

            dateDialog.setMyItemClickListener(object  : DialogDatePicker.MyItemClickListener {
                override fun saveBirthday(birthday: String) {
                    binding.signupGenderRealBirthdayTv.text = birthday
                }

            })
        }

    }

    private fun setTextColor() {  // 글자 파란색, 두껍게 표시
        val text = binding.signupGenderTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(text)  //객체 생성

        val word1 = "성별"
        val start1 = text.indexOf(word1)
        val end1 = start1 + word1.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupGenderTitleTv.text = spannableString

        val word2 = "생년월일"
        val start2 = text.indexOf(word2)
        val end2 = start2 + word2.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupGenderTitleTv.text = spannableString
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() : String {  // 오늘 날짜 불러오기
        val today = System.currentTimeMillis()  // 현재 날짜, 시각 불러오기
        val date = Date(today)
        val mFormat = SimpleDateFormat("yyyy-MM-dd")

        return mFormat.format(date)
    }
}