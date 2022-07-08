package com.example.eraofband.main.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityProfileEditBinding
import com.example.eraofband.signup.DialogDatePicker

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileEditBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditBackIv.setOnClickListener {  // 백 버튼을 누르면 뒤로
            finish()
        }

        // 데베 정보 불러올 자리
        binding.profileEditIntroduceNumTv.text = "0 / 100"  // 나중에 원래 소개 글 글자 수 가지고 올 예정
        binding.profileEditRealBirthdayTv.text = "2001-12-10"  // 여기에 나중에 데베에 저장 된 생일 불러올 예정
        binding.profileEditManCb.isChecked = true  // 여기도 나중에 데베 성별 불러올 예정

        // 소개 글 글자 수 연동
        binding.profileEditIntroduceEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.profileEditIntroduceEt.hint = ""
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.profileEditIntroduceNumTv.text = binding.profileEditIntroduceEt.text.length.toString() + " / 100"
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.profileEditIntroduceEt.hint = ""
            }

        })

        // 성별은 하나만 고를 수 있도록
        binding.profileEditManCb.setOnClickListener {
            if(binding.profileEditWomanCb.isChecked) binding.profileEditWomanCb.isChecked = false
        }

        binding.profileEditWomanCb.setOnClickListener {
            if(binding.profileEditManCb.isChecked) binding.profileEditManCb.isChecked = false
        }

        // 생일 날짜 설정
        binding.profileEditRealBirthdayTv.setOnClickListener {
            // 현재 설정되어있는 날짜를 넘겨줌
            val dateDialog = DialogDatePicker(binding.profileEditRealBirthdayTv.text.toString())
            dateDialog.show(supportFragmentManager, "dateDialog")

            // DialogDatePicker의 날짜 변경 인터페이스를 불러와서 TextView에 날짜를 저장
            dateDialog.setMyItemClickListener(object  : DialogDatePicker.MyItemClickListener {
                override fun saveBirthday(birthday: String) {
                    binding.profileEditRealBirthdayTv.text = birthday
                }

            })
        }

        initSpinner()  // 나중에 지역 설정 할 예정

    }

    private fun initSpinner() {  // 스피너 초기화
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.spinner_item, city)
        binding.profileEditCitySp.adapter = cityAdapter
        binding.profileEditCitySp.setSelection(0)

        // 도시 스피너 클릭 이벤트
        binding.profileEditCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val area = resources.getStringArray(R.array.seoul)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                    binding.profileEditAreaSp.adapter = areaAdapter
                    binding.profileEditAreaSp.setSelection(0)
                }
                else {  // 경기도면 경기도 지역 연결
                    val area = resources.getStringArray(R.array.gyeonggido)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                    binding.profileEditAreaSp.adapter = areaAdapter
                    binding.profileEditAreaSp.setSelection(0)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val area = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.spinner_item, area)
                binding.profileEditAreaSp.adapter = areaAdapter
                binding.profileEditAreaSp.setSelection(0)
            }

        })

    }

}