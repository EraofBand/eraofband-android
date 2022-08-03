package com.example.eraofband.main.home.session.band

import android.annotation.SuppressLint
import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ActivityBandEditBinding
import com.example.eraofband.remote.getBand.GetBandResult
import com.example.eraofband.remote.getBand.GetBandService
import com.example.eraofband.remote.getBand.GetBandView
import com.example.eraofband.remote.patchBand.PatchBandService
import com.example.eraofband.remote.patchBand.PatchBandView
import com.example.eraofband.remote.sendimg.SendImgResponse
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import android.app.TimePickerDialog
import android.content.res.Resources
import android.graphics.Point
import android.os.Message
import android.view.Gravity
import com.example.eraofband.signup.DialogDatePicker


class BandEditActivity : AppCompatActivity(), GetBandView, PatchBandView, SendImgView{

    private lateinit var binding: ActivityBandEditBinding

    private var currentArea = 0 //스피너 초기화
    private var initial = true

    private var band = Band("", "", "", "", "", 0, "", "", 0,
        "", 0, "", 0, "", "", 0, "",  "", "",
        0, 0, "")
    private var bandIdx = 0
    private var profileImgUrl = ""

    private var vocalCnt = 0
    private var guitarCnt = 0
    private var baseCnt = 0
    private var keyboardCnt = 0
    private var drumCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bandIdx = intent.getIntExtra("bandIdx", 0)

        binding.homeBandEditBackIb.setOnClickListener { finish() }

        binding.homeBandEditImgV.setOnClickListener {
            initImageViewBand()
        }

        binding.homeBandEditNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandEditNameCntTv.text = binding.homeBandEditNameEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandEditNameEt.hint = ""
            }
        })

        binding.homeBandEditInfoEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandEditInfoCntTv.text = binding.homeBandEditInfoEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandEditInfoEt.hint = ""
            }
        })

        binding.homeBandEditDetailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandEditDetailCntTv.text = binding.homeBandEditDetailEt.text.length.toString() + " / 500"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandEditDetailEt.hint = ""
            }
        })

        binding.homeBandShowNameEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandShowNameCntTv.text = binding.homeBandShowNameEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandShowNameEt.hint = ""
            }
        })

        binding.homeBandShowLocationEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandShowLocationCntTv.text = binding.homeBandShowLocationEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandShowLocationEt.hint = ""
            }
        })

        binding.homeBandShowFeeEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandShowFeeCntTv.text = binding.homeBandShowFeeEt.text.length.toString() + " / 10"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandShowFeeEt.hint = ""
            }
        })


        binding.homeBandEditRegisterBtn.setOnClickListener {
            val patchBandService = PatchBandService()
            patchBandService.setPatchView(this)

            band = updateBand()
            Log.d("BAND PATCH", band.toString())

            patchBandService.patchBand(getJwt()!!, bandIdx, band)

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 100)
        }


        initVocalCnt()
        initGuitarCnt()
        initBaseCnt()
        initKeyboardCnt()
        initDrumCnt()
        initDatepicker()
        initTimepicker()

    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun updateBand(): Band {
        band.bandTitle = binding.homeBandEditNameEt.text.toString()
        band.bandIntroduction = binding.homeBandEditInfoEt.text.toString()
        band.bandContent = binding.homeBandEditDetailEt.text.toString()

        band.chatRoomLink = binding.homeBandEditChatEt.text.toString()
        band.bandRegion = binding.homeBandEditCitySp.selectedItem.toString() + " " + binding.homeBandEditAreaSp.selectedItem.toString()

        band.bandImgUrl = profileImgUrl
        band.userIdx = getUserIdx()

        band.vocalComment = binding.homeBandEditVocalEt.text.toString()
        band.guitarComment = binding.homeBandEditGuitarEt.text.toString()
        band.baseComment = binding.homeBandEditBaseEt.text.toString()
        band.keyboardComment = binding.homeBandEditKeyboardEt.text.toString()
        band.drumComment = binding.homeBandEditDrumEt.text.toString()

        band.performTitle = binding.homeBandShowNameEt.text.toString()
        val performFee = binding.homeBandShowFeeEt.text.toString()
        band.performFee = performFee.toInt()
        band.performLocation = binding.homeBandShowLocationEt.text.toString()

        band.performDate = binding.homeBandShowDateEt.text.toString()
        band.performTime = binding.homeBandShowTimeEt.text.toString()

        return band
    }

    override fun onResume(){
        super.onResume()

        val bandService = GetBandService()
        bandService.setBandView(this)
        bandService.getBand(getJwt()!!, intent.getIntExtra("bandIdx", 0))
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initTimepicker() {
        binding.homeBandShowTimeEt.setOnClickListener {
            val dateDialog = DialogTimePicker(binding.homeBandShowTimeEt.text.toString())
            dateDialog.show(supportFragmentManager, "timeDialog")

            // DialogDatePicker의 날짜 변경 인터페이스를 불러와서 TextView에 날짜를 저장
            dateDialog.setMyItemClickListener(object  : DialogTimePicker.MyItemClickListener {
                override fun saveShow(time: String) {
                    binding.homeBandShowTimeEt.text = time
                }

            })
        }
    }

    private fun initDatepicker() {
        binding.homeBandShowDateEt.text = "2022-07-01"

        binding.homeBandShowDateEt.setOnClickListener {
            // 현재 설정되어있는 날짜를 넘겨줌
            val dateDialog = DialogDatePickerDark(binding.homeBandShowDateEt.text.toString())
            dateDialog.show(supportFragmentManager, "dateDialogDark")

            // DialogDatePicker의 날짜 변경 인터페이스를 불러와서 TextView에 날짜를 저장
            dateDialog.setMyItemClickListener(object  : DialogDatePickerDark.MyItemClickListener {
                override fun saveShow(date: String) {
                    binding.homeBandShowDateEt.text = date
                }

            })
        }
    }


    private fun initVocalCnt() {
        var cnt = vocalCnt
        binding.editVocalCntTv.text = cnt.toString()
        binding.editVocalPlusIb.setOnClickListener {
            cnt += 1
            binding.editVocalCntTv.text = cnt.toString()
        }
        binding.editVocalMinusIb.setOnClickListener {
            if(cnt != vocalCnt){
                cnt -= 1
                binding.editVocalCntTv.text = cnt.toString()
            } else{
                binding.editVocalCntTv.text = cnt.toString()
            }
        }
    }

    private fun initGuitarCnt() {
        var cnt = guitarCnt
        binding.editGuitarCntTv.text = cnt.toString()
        binding.editGuitarPlusIb.setOnClickListener {
            cnt += 1
            binding.editGuitarCntTv.text = cnt.toString()
        }
        binding.editGuitarMinusIb.setOnClickListener {
            if(cnt != guitarCnt){
                cnt -= 1
                binding.editGuitarCntTv.text = cnt.toString()
            } else{
                binding.editGuitarCntTv.text = cnt.toString()
            }
        }
    }

    private fun initBaseCnt() {
        var cnt = baseCnt
        binding.editBaseCntTv.text = cnt.toString()
        binding.editBasePlusIb.setOnClickListener {
            cnt += 1
            binding.editBaseCntTv.text = cnt.toString()
        }
        binding.editBaseMinusIb.setOnClickListener {
            if(cnt != baseCnt){
                cnt -= 1
                binding.editBaseCntTv.text = cnt.toString()
            } else{
                binding.editBaseCntTv.text = cnt.toString()
            }
        }
    }

    private fun initKeyboardCnt() {
        var cnt = keyboardCnt
        binding.editKeyboardCntTv.text = cnt.toString()
        binding.editKeyboardPlusIb.setOnClickListener {
            cnt += 1
            binding.editKeyboardCntTv.text = cnt.toString()
        }
        binding.editKeyboardMinusIb.setOnClickListener {
            if(cnt != keyboardCnt){
                cnt -= 1
                binding.editKeyboardCntTv.text = cnt.toString()
            } else{
                binding.editKeyboardCntTv.text = cnt.toString()
            }
        }
    }

    private fun initDrumCnt() {
        var cnt = drumCnt
        binding.editDrumCntTv.text = cnt.toString()
        binding.editDrumPlusIb.setOnClickListener {
            cnt += 1
            binding.editDrumCntTv.text = cnt.toString()
        }
        binding.editDrumMinusIb.setOnClickListener {
            if(cnt != drumCnt){
                cnt -= 1
                binding.editDrumCntTv.text = cnt.toString()
            } else{
                binding.editDrumCntTv.text = cnt.toString()
            }
        }
    }

    private fun initRegion(region: String) {  // 지역 설정
        // 도시 스피너 어뎁터 연결
        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, resources.getStringArray(R.array.city))
        binding.homeBandEditCitySp.adapter = cityAdapter

        val array = region.split(" ")

        val city = array[0]
        val area = array[1]

        val areaList: Array<String>

        if(city == "서울") {
            binding.homeBandEditCitySp.setSelection(0)
            areaList = resources.getStringArray(R.array.seoul)
        }
        else {
            binding.homeBandEditCitySp.setSelection(1)
            areaList = resources.getStringArray(R.array.gyeonggido)
        }


        // 지역 스피너 어뎁터 연결
        val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, areaList)
        binding.homeBandEditAreaSp.adapter = areaAdapter

        // 해당 지역의 스피너 위치를 찾음
        for(i in 0..areaList.size) {
            if(area == areaList[i]) {
                binding.homeBandEditAreaSp.setSelection(i)
                currentArea = i  // 현재 지역 지정
                break
            }
        }
    }

    private fun spinnerClickListener() {
        // 도시 스피너 클릭 이벤트
        binding.homeBandEditCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, resources.getStringArray(R.array.seoul))
                    binding.homeBandEditAreaSp.adapter = areaAdapter

                    if(initial) {
                        binding.homeBandEditAreaSp.setSelection(currentArea)
                        initial = false
                    }
                    else {
                        binding.homeBandEditAreaSp.setSelection(0)
                    }
                }
                else {  // 경기도면 경기도 지역 연결
                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, resources.getStringArray(R.array.gyeonggido))
                    binding.homeBandEditAreaSp.adapter = areaAdapter

                    if(initial) {
                        binding.homeBandEditAreaSp.setSelection(currentArea)
                        initial = false
                    }
                    else {
                        binding.homeBandEditAreaSp.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val areaList = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, areaList)
                binding.homeBandEditAreaSp.adapter = areaAdapter

                binding.homeBandEditAreaSp.setSelection(0)
            }
        })
    }

    private fun initImageViewBand() {
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
                        .into(binding.homeBandEditImgV)

                    val imgPath = absolutelyPath(selectedImageUri, this)
                    val file = File(imgPath)
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    val sendImgService = SendImgService()
                    sendImgService.setImageView(this)
                    sendImgService.sendImg(body)

                    binding.homeBandEditAddImgTv.visibility = View.INVISIBLE
                    binding.homeBandEditAddInfoImgTv.visibility = View.INVISIBLE
                    binding.homeBandEditImgIv.visibility = View.INVISIBLE
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

    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result!!
    }

    override fun onGetSuccess(result: GetBandResult) {
        Log.d("GET BAND /SUCCESS", result.toString())

        profileImgUrl = result.bandImgUrl
        Glide.with(this)
            .load(result.bandImgUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.homeBandEditImgV)
        binding.homeBandEditImgV.clipToOutline = true

        binding.homeBandEditAddImgTv.visibility = View.INVISIBLE
        binding.homeBandEditAddInfoImgTv.visibility = View.INVISIBLE
        binding.homeBandEditImgIv.visibility = View.INVISIBLE

        binding.homeBandEditNameEt.setText(result.bandTitle)
        binding.homeBandEditInfoEt.setText(result.bandIntroduction)

        binding.homeBandEditVocalEt.setText(result.vocalComment)
        binding.homeBandEditGuitarEt.setText(result.guitarComment)
        binding.homeBandEditBaseEt.setText(result.baseComment)
        binding.homeBandEditKeyboardEt.setText(result.keyboardComment)
        binding.homeBandEditDrumEt.setText(result.drumComment)


        binding.homeBandEditDetailEt.setText(result.bandContent)
        binding.homeBandEditChatEt.setText(result.chatRoomLink)

        vocalCnt = result.vocal
        guitarCnt = result.guitar
        baseCnt = result.base
        keyboardCnt = result.keyboard
        drumCnt = result.drum

        initRegion(result.bandRegion)
        spinnerClickListener()

    }

    private fun setToast(msg : String) {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = msg

        val display = windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기
        val height = size.y / 2  // 토스트 메세지가 중간에 고정되어있기 때문에 높이 / 2

        // 중간부터 marginBottom, 버튼 높이 / 2 만큼 빼줌
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 60.toPx() - binding.homeBandEditRegisterBtn.height / 2)
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET BAND / FAIL", "$code $message")
    }

    override fun onPatchSuccess(code: Int, result: String) {
        Log.d("PATCH / SUCCESS", result)
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("PATCH / FAIL", "$code $message")
        setToast(message)
    }

    override fun onSendSuccess(response: SendImgResponse) {
        Log.d("SEND IMG / SUCCESS", response.result)
        profileImgUrl = response.result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SEND IMG / FAIL", "$code $message")
    }
}