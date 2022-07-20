package com.example.eraofband.main.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.EditUser
import com.example.eraofband.databinding.ActivityProfileEditBinding
import com.example.eraofband.remote.getMyPage.GetMyPageResult
import com.example.eraofband.remote.getMyPage.GetMyPageService
import com.example.eraofband.remote.getMyPage.GetMyPageView
import com.example.eraofband.remote.patchuser.PatchUserResult
import com.example.eraofband.remote.patchuser.PatchUserService
import com.example.eraofband.remote.patchuser.PatchUserView
import com.example.eraofband.remote.sendimg.SendImgResponse
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import com.example.eraofband.signup.DialogDatePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileEditActivity : AppCompatActivity(), GetMyPageView, PatchUserView, SendImgView {

    private lateinit var binding : ActivityProfileEditBinding
    private var editUser = EditUser("", "", "", "", "", "", 0)
    private var profileUrl = ""

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
        val getMyPageService = GetMyPageService()

        getMyPageService.setUserView(this)
        getMyPageService.getUser(getJwt()!!, getUserIdx())

        binding.profileEditCameraIv.setOnClickListener {
            initImageViewProfile()
        }

        // 소개 글 글자 수 실시간 연동
        binding.profileEditIntroduceEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.profileEditIntroduceEt.hint = "이거 때문인가?"
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.profileEditIntroduceNumTv.text = binding.profileEditIntroduceEt.text.length.toString() + " / 100"
            }

            override fun afterTextChanged(p0: Editable?) {
                editUser.introduction = binding.profileEditIntroduceEt.text.toString()
                binding.profileEditIntroduceEt.hint = ""
            }

        })

        // 라디오그룹 클릭 리스너 <- 나중에 프로필 편집 api 연동할 때 사용하시면 돼용
        binding.profileEditGenderRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.profile_edit_man_rb -> editUser.gender = "MALE"
                R.id.profile_edit_woman_rb -> editUser.gender = "FEMALE"
            }
        }


        binding.signupSaveBtn.setOnClickListener {
            val patchUserService = PatchUserService()
            patchUserService.setPatchUserView(this)

            editUser = updateUser()
            Log.d("USER PATCH", editUser.toString())

            patchUserService.patchUser(getJwt()!!, editUser)
            finish()
        }
    }

    private fun updateUser(): EditUser {
        editUser.birth = binding.profileEditRealBirthdayTv.text.toString()
        editUser.nickName = binding.profileEditNicknameEt.text.toString()
        editUser.profileImgUrl = profileUrl
        editUser.userIdx = getUserIdx()
        editUser.region = binding.profileEditCitySp.selectedItem.toString() + " " + binding.profileEditAreaSp.selectedItem.toString()

        return editUser
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initSpinner() {  // 스피너 초기화
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.profileEditCitySp.adapter = cityAdapter
        binding.profileEditCitySp.setSelection(0)

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
    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        // Glide로 이미지 표시하기
        // Glide로 이미지 표시하기
        profileUrl = result.getUser.profileImgUrl
        Glide.with(this).load(profileUrl).into(binding.profileEditProfileIv)

        binding.profileEditNicknameEt.setText(result.getUser.nickName)  // 닉네임 연결

        binding.profileEditIntroduceEt.setText(result.getUser.introduction)  // 내 소개 연결
        binding.profileEditIntroduceNumTv.text = binding.profileEditIntroduceEt.text.length.toString() + "/ 100"  // 내 소개 글자 수 연결

        if(result.getUser.gender == "MALE") binding.profileEditManRb.isChecked = true  // 성별 연결
        else binding.profileEditWomanRb.isChecked = true

        binding.profileEditRealBirthdayTv.text = result.getUser.birth  // 생일 연결

        findRegion(result.getUser.region)  // 지역 연결

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

    private fun initImageViewProfile() {
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
                        .apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileEditProfileIv)

                    val imgPath = absolutelyPath(selectedImageUri, this)
                    val file = File(imgPath)
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    val sendImgService = SendImgService()
                    sendImgService.setImageView(this)
                    sendImgService.sendImg(body)

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
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result!!
    }

    override fun onPatchSuccess(code: Int, result: PatchUserResult) {
        Log.d("PATCH / SUCCESS", result.toString())
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("PATCH / FAIL", "$code $message")
    }

    override fun onSendSuccess(response: SendImgResponse) {
        Log.d("SENDIMGss", response.toString())
        profileUrl = response.result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMGss", "$code $message")
    }
}