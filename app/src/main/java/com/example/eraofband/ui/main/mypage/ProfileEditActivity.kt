package com.example.eraofband.ui.main.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.EditUser
import com.example.eraofband.databinding.ActivityProfileEditBinding
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import com.example.eraofband.remote.user.getMyPage.GetMyPageResult
import com.example.eraofband.remote.user.getMyPage.GetMyPageService
import com.example.eraofband.remote.user.getMyPage.GetMyPageView
import com.example.eraofband.remote.user.patchUser.PatchUserService
import com.example.eraofband.remote.user.patchUser.PatchUserView
import com.example.eraofband.ui.setOnSingleClickListener
import com.example.eraofband.ui.signup.DialogDatePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileEditActivity : AppCompatActivity(), GetMyPageView, PatchUserView, SendImgView {

    private lateinit var binding : ActivityProfileEditBinding

    private var editUser = EditUser("", "", "", "", "", "", 0)
    private var profileUrl = ""
    private lateinit var getResult: ActivityResultLauncher<Intent>

    // 스피너 초기화 관련 변수
    private var initial = true
    private var currentArea = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditBackIv.setOnClickListener {  // 백 버튼을 누르면 뒤로
            finish()
        }

        // 유저 정보를 받아온 후 프로필 편집 화면에 연동
        val getMyPageService = GetMyPageService()

        getMyPageService.setUserView(this)
        getMyPageService.getMyInfo(getJwt()!!, getUserIdx())

        getImage()

        binding.profileEditCameraIv.setOnSingleClickListener {  // 프사 변경을 원하면 갤러리 접근하도록
            initImageViewProfile()
        }
        binding.profileEditProfileIv.setOnSingleClickListener {
            initImageViewProfile()
        }

        // 소개 글 글자 수 실시간 연동
        binding.profileEditIntroduceEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.profileEditIntroduceEt.hint = "."
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

        initDatePicker()

        binding.signupSaveBtn.setOnSingleClickListener {
            val patchUserService = PatchUserService()
            patchUserService.setPatchUserView(this)

            editUser = updateUser()
            Log.d("USER PATCH", editUser.toString())

            patchUserService.patchUser(getJwt()!!, editUser)

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 100)
        }
    }

    private fun updateUser(): EditUser {
        editUser.birth = "${binding.profileEditRealBirthdayTv.text.trim()}"
        editUser.nickName = "${binding.profileEditNicknameEt.text.trim()}"
        editUser.profileImgUrl = profileUrl
        editUser.userIdx = getUserIdx()
        editUser.region = "${binding.profileEditCitySp.selectedItem}" + " " + "${binding.profileEditAreaSp.selectedItem}"

        return editUser
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        Log.d("jwt value", userSP.getString("jwt", "").toString())
        return userSP.getString("jwt", "")
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

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        // Glide로 이미지 표시하기
        profileUrl = result.getUser.profileImgUrl
        Glide.with(this).load(profileUrl)
            .apply(RequestOptions.centerCropTransform())
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profileEditProfileIv)

        binding.profileEditNicknameEt.setText(result.getUser.nickName)  // 닉네임 연결

        binding.profileEditIntroduceEt.setText(result.getUser.introduction)  // 내 소개 연결
        binding.profileEditIntroduceNumTv.text = binding.profileEditIntroduceEt.text.length.toString() + "/ 100"  // 내 소개 글자 수 연결

        if(result.getUser.gender == "MALE") binding.profileEditManRb.isChecked = true  // 성별 연결
        else binding.profileEditWomanRb.isChecked = true

        binding.profileEditRealBirthdayTv.text = result.getUser.birth  // 생일 연결

        initRegion(result.getUser.region) // 지역 연결
        spinnerClickListener()

        Log.d("GETUSER/SUC", result.toString())
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETUSER/FAIL", "$code $message")
    }

    private fun initRegion(region: String) {  // 지역 설정
        // 도시 스피너 어뎁터 연결
        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, resources.getStringArray(R.array.city))
        binding.profileEditCitySp.adapter = cityAdapter

        val array = region.split(" ")

        val city = array[0]
        val area = array[1]

        val areaList: Array<String> = if(city == "서울") {
            binding.profileEditCitySp.setSelection(0)
            resources.getStringArray(R.array.seoul)
        } else {
            binding.profileEditCitySp.setSelection(1)
            resources.getStringArray(R.array.gyeonggido)
        }


        // 지역 스피너 어뎁터 연결
        val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, areaList)
        binding.profileEditAreaSp.adapter = areaAdapter

        // 해당 지역의 스피너 위치를 찾음
        for(i in 0..areaList.size) {
            if(area == areaList[i]) {
                binding.profileEditAreaSp.setSelection(i)
                currentArea = i  // 현재 지역 지정
                break
            }
        }
    }

    private fun spinnerClickListener() {
        // 도시 스피너 클릭 이벤트
        binding.profileEditCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, resources.getStringArray(R.array.seoul))
                    binding.profileEditAreaSp.adapter = areaAdapter

                    if(initial) {
                        binding.profileEditAreaSp.setSelection(currentArea)
                        initial = false
                    }
                    else {
                        binding.profileEditAreaSp.setSelection(0)
                    }
                }
                else {  // 경기도면 경기도 지역 연결
                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, resources.getStringArray(R.array.gyeonggido))
                    binding.profileEditAreaSp.adapter = areaAdapter

                    if(initial) {
                        binding.profileEditAreaSp.setSelection(currentArea)
                        initial = false
                    }
                    else {
                        binding.profileEditAreaSp.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val areaList = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, areaList)
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

    private fun initImageViewProfile() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getResult.launch(intent)
        }
        // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
        else requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    getResult.launch(intent)
                }
                else Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun getImage() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                Log.d("RESULT", result.data.toString())
                val selectedImageUri: Uri? = result.data?.data

                if (selectedImageUri != null) {
                    Glide.with(this).load(selectedImageUri)
                        .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
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
        }
    }

    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result!!
    }

    override fun onPatchSuccess(code: Int, result: String) {
        Log.d("PATCH / SUCCESS",  result)
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("PATCH / FAIL", "$code $message")
    }

    override fun onSendSuccess(result: String) {
        Log.d("SENDING / FAIL", result)
        profileUrl = result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIBGss / FAIL", "$code $message")
    }
}