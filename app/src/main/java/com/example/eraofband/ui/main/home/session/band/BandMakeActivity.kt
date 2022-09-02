package com.example.eraofband.ui.main.home.session.band

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ActivityBandMakeBinding
import com.example.eraofband.remote.band.makeBand.MakeBandResult
import com.example.eraofband.remote.band.makeBand.MakeBandService
import com.example.eraofband.remote.band.makeBand.MakeBandView
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class BandMakeActivity : AppCompatActivity(), MakeBandView, SendImgView {

    private lateinit var binding: ActivityBandMakeBinding
    private var band = Band("", "", "", "", "", 0, "", "", 0,
    "", 0, "", 0, "", "", 0, "","", "",
        0, 0, "")

    private var imgUrl = ""
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val makeService = MakeBandService()
        makeService.setMakeView(this)

        getImage()

        binding.homeBandMakeImgIv.setOnClickListener {
            initImageViewBand()
        }
        binding.homeBandMakeAddImgTv.setOnClickListener {
            initImageViewBand()
        }
        binding.homeBandMakeImgV.setOnClickListener {
            initImageViewBand()
        }

        binding.homeBandMakeNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandMakeNameCntTv.text = binding.homeBandMakeNameEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandMakeNameEt.hint = ""
            }
        })

        binding.homeBandMakeInfoEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandMakeInfoCntTv.text = binding.homeBandMakeInfoEt.text.length.toString() + " / 20"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandMakeInfoEt.hint = ""
            }
        })

        binding.homeBandMakeDetailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.homeBandMakeDetailCntTv.text = binding.homeBandMakeDetailEt.text.length.toString() + " / 500"
            }
            override fun afterTextChanged(p0: Editable?) {
                binding.homeBandMakeDetailEt.hint = ""
            }
        })

        binding.homeBandMakeBackIb.setOnClickListener { finish() }

        binding.homeBandMakeRegisterBtn.setOnClickListener {
            Log.d("BAND MAKE", postBand().toString())
            makeService.makeBand(getJwt()!!, postBand())
        }

        initSpinner()
        initVocalCnt()
        initGuitarCnt()
        initBaseCnt()
        initKeyboardCnt()
        initDrumCnt()

        //채팅방, 세션 et는 빼놨습니다
    }

    private fun postBand() : Band{
        band.bandTitle = "${binding.homeBandMakeNameEt.text.trim()}"
        band.bandIntroduction = "${binding.homeBandMakeInfoEt.text.trim()}"
        band.bandContent = "${binding.homeBandMakeDetailEt.text.trim()}"
        band.chatRoomLink = "${binding.homeBandMakeChatEt.text.trim()}"
        band.bandRegion =  "${binding.homeBandMakeCitySp.selectedItem}" + " " + "${binding.homeBandMakeAreaSp.selectedItem}"
        band.bandImgUrl = imgUrl

        band.vocalComment = "${binding.homeBandMakeVocalEt.text.trim()}"
        band.guitarComment = "${binding.homeBandMakeGuitarEt.text.trim()}"
        band.baseComment = "${binding.homeBandMakeBaseEt.text.trim()}"
        band.keyboardComment = "${binding.homeBandMakeKeyboardEt.text.trim()}"
        band.drumComment = "${binding.homeBandMakeDrumEt.text.trim()}"

        band.userIdx = getUserIdx()

        return band
    }

    private fun initVocalCnt() {
        var cnt = 0
        binding.makeVocalCntTv.text = cnt.toString()
        binding.makeVocalPlusIb.setOnClickListener {
            cnt += 1
            binding.makeVocalCntTv.text = cnt.toString()
            band.vocal = cnt
        }
        binding.makeVocalMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeVocalCntTv.text = cnt.toString()
                band.vocal = cnt
            } else{
                binding.makeVocalCntTv.text = cnt.toString()
            }
        }
    }

    private fun initGuitarCnt() {
        var cnt = 0
        binding.makeGuitarCntTv.text = cnt.toString()
        binding.makeGuitarPlusIb.setOnClickListener {
            cnt += 1
            binding.makeGuitarCntTv.text = cnt.toString()
            band.guitar = cnt
        }
        binding.makeGuitarMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeGuitarCntTv.text = cnt.toString()
                band.guitar = cnt
            } else{
                binding.makeGuitarCntTv.text = cnt.toString()
            }
        }
    }

    private fun initBaseCnt() {
        var cnt = 0
        binding.makeBaseCntTv.text = cnt.toString()
        binding.makeBasePlusIb.setOnClickListener {
            cnt += 1
            binding.makeBaseCntTv.text = cnt.toString()
            band.base = cnt
        }
        binding.makeBaseMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeBaseCntTv.text = cnt.toString()
                band.base = cnt
            } else{
                binding.makeBaseCntTv.text = cnt.toString()
            }
        }

    }

    private fun initKeyboardCnt() {
        var cnt = 0
        binding.makeKeyboardCntTv.text = cnt.toString()
        binding.makeKeyboardPlusIb.setOnClickListener {
            cnt += 1
            binding.makeKeyboardCntTv.text = cnt.toString()
            band.keyboard = cnt
        }
        binding.makeKeyboardMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeKeyboardCntTv.text = cnt.toString()
                band.keyboard = cnt
            } else{
                binding.makeKeyboardCntTv.text = cnt.toString()
            }
        }

    }

    private fun initDrumCnt() {
        var cnt = 0
        binding.makeDrumCntTv.text = cnt.toString()
        binding.makeDrumPlusIb.setOnClickListener {
            cnt += 1
            binding.makeDrumCntTv.text = cnt.toString()
            band.drum = cnt
        }
        binding.makeDrumMinusIb.setOnClickListener {
            if(cnt != 0){
                cnt -= 1
                binding.makeDrumCntTv.text = cnt.toString()
                band.drum = cnt
            } else{
                binding.makeDrumCntTv.text = cnt.toString()
            }
        }
    }

    private fun initSpinner() {
        // 도시 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.city)  // 도시 목록

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.homeBandMakeCitySp.adapter = cityAdapter
        binding.homeBandMakeCitySp.setSelection(0)

        // 도시 스피너 클릭 이벤트
        binding.homeBandMakeCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 서울이면 서울시 지역 연결
                    val area = resources.getStringArray(R.array.seoul)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.homeBandMakeAreaSp.adapter = areaAdapter
                }
                else {  // 경기도면 경기도 지역 연결
                    val area = resources.getStringArray(R.array.gyeonggido)

                    val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                    binding.homeBandMakeAreaSp.adapter = areaAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 서울 지역을 띄워줌
                val area = resources.getStringArray(R.array.seoul)

                val areaAdapter = ArrayAdapter(applicationContext, R.layout.item_spinner, area)
                binding.homeBandMakeAreaSp.adapter = areaAdapter
                binding.homeBandMakeAreaSp.setSelection(0)
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

    private fun initImageViewBand() {
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
                    Glide.with(this)
                        .load(selectedImageUri)
                        .transform(CenterCrop(), RoundedCorners(15))
                        .into(binding.homeBandMakeImgV)

                    val imgPath = absolutelyPath(selectedImageUri, this)
                    val file = File(imgPath)
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    val sendImgService = SendImgService()
                    sendImgService.setImageView(this)
                    sendImgService.sendImg(body)

                    binding.homeBandMakeAddImgTv.visibility = View.INVISIBLE
                    binding.homeBandMakeAddInfoImgTv.visibility = View.INVISIBLE
                    binding.homeBandMakeImgIv.visibility = View.INVISIBLE

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
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 60.toPx() - binding.homeBandMakeRegisterBtn.height / 2)
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx(): Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    override fun onMakeSuccess(code: Int, result: MakeBandResult) {
        Log.d("MAKE BAND / SUCCESS", result.toString())
        finish()
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE BAND / FAIL","$code $message")
        setToast(message)
    }

    override fun onSendSuccess(result: String) {
        Log.d("SENDIMG/SUC", result)
        imgUrl = result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMG/FAIL", "$code $message")
    }
}