package com.example.eraofband.main.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityProfileEditBinding
import com.example.eraofband.remote.GetUserResult
import com.example.eraofband.remote.GetUserService
import com.example.eraofband.remote.GetUserView
import com.example.eraofband.signup.DialogDatePicker

class ProfileEditActivity : AppCompatActivity(), GetUserView {

    private lateinit var binding : ActivityProfileEditBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditBackIv.setOnClickListener {  // 백 버튼을 누르면 뒤로
            finish()
        }

        initSpinner()  // 스피너 초기화
        initDatePicker()

        // 유저 정보를 받아온 후 프로필 편집 화면에 연동
        val getUserService = GetUserService()

        getUserService.setUserView(this)
        getUserService.getUser(5)

        // 소개 글 글자 수 실시간 연동
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

        // 라디오그룹 클릭 리스너 <- 나중에 프로필 편집 api 연동할 때 사용하시면 돼용
        binding.profileEditGenderRg.setOnCheckedChangeListener { _, id ->
            when (id) {
//                R.id.profile_edit_man_rb -> Toast.makeText(applicationContext, "남자 선택", Toast.LENGTH_SHORT).show()
//                R.id.profile_edit_woman_rb -> Toast.makeText(applicationContext, "여자 선택", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initSpinner() {  // 스피너 초기화
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.profileEditCitySp.adapter = cityAdapter

        // 도시 스피너 클릭 이벤트
        binding.profileEditCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val area = resources.getStringArray(R.array.seoul)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.profileEditAreaSp.adapter = areaAdapter
                }
                else {  // 경기도면 경기도 지역 연결
                    val area = resources.getStringArray(R.array.gyeonggido)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.profileEditAreaSp.adapter = areaAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val area = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                binding.profileEditAreaSp.adapter = areaAdapter
                binding.profileEditAreaSp.setSelection(0)
            }

        })

    }

    private fun initDatePicker() {
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
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetUserResult) {
        binding.profileEditNicknameEt.setText(result.nickName)  // 닉네임 연결

        binding.profileEditIntroduceEt.setText(result.instroduction)  // 내 소개 연결
        binding.profileEditIntroduceNumTv.text = binding.profileEditIntroduceEt.text.length.toString() + "/ 100"  // 내 소개 글자 수 연결

        if(result.gender == "MALE") binding.profileEditManRb.isChecked = true  // 성별 연결
        else binding.profileEditWomanRb.isChecked = true

        binding.profileEditRealBirthdayTv.text = result.birth  // 생일 연결

        findRegion(result.region)  // 지역 연결

        Log.d("GETUSER/SUC", result.toString())
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETUSER/FAIL", "$code $message")
    }

    private fun findRegion(region: String) {
        // 서울, 경기도와 구, 시를 구분
        val index = region.indexOf(" ")
        val city = region.substring(0, index)  // 서울, 경기도
        val area = region.substring(index + 1)

        val areaList : Array<String>

        if(city == "서울") {  // 서울인 경우
            binding.profileEditCitySp.setSelection(0)
            areaList = resources.getStringArray(R.array.seoul)
        }
        else {  // 경기도인 경우
            binding.profileEditCitySp.setSelection(1)
            areaList = resources.getStringArray(R.array.gyeonggido)
        }

        // 해당 지역의 스피너 위치를 찾음
        for(i in 0..areaList.size) {
            if(area == areaList[i]) {
                binding.profileEditAreaSp.setSelection(i)
                break
            }
        }
    }
}