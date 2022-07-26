package com.example.eraofband.main.home.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityLessonMakeBinding

class LessonMakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonMakeBackIb.setOnClickListener { finish() }

        binding.homeLessonMakeAddImgTv.setOnClickListener {
            initImageViewLesson()
        }
        binding.homeLessonMakeImgIv.setOnClickListener {
            initImageViewLesson()
        }

        binding.homeLessonMakeNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeLessonMakeNameCntTv.text = binding.homeLessonMakeNameEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeLessonMakeNameEt.hint = ""
            }
        })

        binding.homeLessonMakeInfoEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeLessonMakeInfoCntTv.text = binding.homeLessonMakeInfoEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeLessonMakeInfoEt.hint = ""
            }
        })

        binding.homeLessonMakeDetailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeLessonMakeDetailCntTv.text = binding.homeLessonMakeDetailEt.text.length.toString() + " / 100"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeLessonMakeDetailEt.hint = ""
            }
        })


        initLessonSpinner()
        initSpinner()
        initImageViewLesson()
        initCnt()
    }

    private fun initLessonSpinner() {
        val lesson = resources.getStringArray(R.array.lesson)  // 도시 목록

        val lessonAdapter = ArrayAdapter(this, R.layout.item_spinner, lesson)
        binding.homeLessonMakeTypeSp.adapter = lessonAdapter
        binding.homeLessonMakeTypeSp.setSelection(0)
    }

    private fun initCnt() {
        var cnt = 0
        binding.makeCntTv.text = cnt.toString()
        binding.makeCntPlusIb.setOnClickListener {
            cnt += 1
            binding.makeCntTv.text = cnt.toString()
        }
        binding.makeCntMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeCntTv.text = cnt.toString()
            } else{
                binding.makeCntTv.text = cnt.toString()
            }
        }
    }

    private fun initSpinner() {
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.homeLessonMakeCitySp.adapter = cityAdapter
        binding.homeLessonMakeCitySp.setSelection(0)

        // 도시 스피너 클릭 이벤트
        binding.homeLessonMakeCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val area = resources.getStringArray(R.array.seoul)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.homeLessonMakeAreaSp.adapter = areaAdapter
                }
                else {  // 경기도면 경기도 지역 연결
                    val area = resources.getStringArray(R.array.gyeonggido)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.homeLessonMakeAreaSp.adapter = areaAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val area = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                binding.homeLessonMakeAreaSp.adapter = areaAdapter
                binding.homeLessonMakeAreaSp.setSelection(0)
            }

        })
    }

    private fun initImageViewLesson() {
        when {
            // 갤러리 접근 권한이 있는 경우
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                navigateGallery()
            }

            // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                showPermissionContextPopup()
            }

            // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
            else -> requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )
        }
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                // 이미지 가져오기 성공하면 원래 이미지를 없애고 가져온 사진을 넣어줌
                // 이미지 동그랗게 + CenterCrop
                if (selectedImageUri != null) {
                    Glide.with(this)
                        .load(selectedImageUri)
                        .transform(CenterCrop(), RoundedCorners(15))
                        .into(binding.homeLessonMakeImgV)

                    binding.homeLessonMakeAddImgTv.visibility = View.INVISIBLE
                    binding.homeLessonMakeAddInfoImgTv.visibility = View.INVISIBLE
                    binding.homeLessonMakeImgIv.visibility = View.INVISIBLE
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        // 권한 확인 용 팝업창
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("대표 사진을 추가하기 위해서는 이미지 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }
}