package com.example.eraofband.ui.main.home.session.band.album

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eraofband.R
import com.example.eraofband.data.Album
import com.example.eraofband.databinding.ActivityBandMakeAlbumBinding
import com.example.eraofband.remote.band.makeAlbumBand.MakeAlbumBandResult
import com.example.eraofband.remote.band.makeAlbumBand.MakeAlbumBandService
import com.example.eraofband.remote.band.makeAlbumBand.MakeAlbumBandView
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import com.example.eraofband.ui.main.home.session.band.DialogDatePickerDark
import com.example.eraofband.ui.setOnSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDate

class BandMakeAlbumActivity : AppCompatActivity(), SendImgView, MakeAlbumBandView {

    private lateinit var binding: ActivityBandMakeAlbumBinding
    private var album = Album("","","",0,0)
    private var imgUrl = ""
    private var bandIdx: Int? = null

    private lateinit var getResult: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBandMakeAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bandIdx = intent.getIntExtra("bandIdx", 0)

        binding.albumMakeBackIb.setOnClickListener {  // 뒤로가기
            finish()
        }

        val makeService = MakeAlbumBandService()
        makeService.setAlbumBandView(this)

        getImage()

        binding.albumMakeVideoCl.setOnSingleClickListener {
            initImageViewBand()
        }
        binding.albumThumbnailIv.setOnSingleClickListener {
            initImageViewBand()
        }
        binding.albumMakeSaveBt.setOnSingleClickListener {
            initAlbum()
            makeService.makeAlbumBand(getJwt()!!, album)
        }

        initDatePicker()
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
                    Glide.with(this).load(selectedImageUri)
                        .transform(CenterCrop(), RoundedCorners(15))
                        .into(binding.albumThumbnailIv)

                    binding.albumMakeVideoCl.visibility = View.INVISIBLE
                    binding.albumThumbnailIv.visibility = View.VISIBLE

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDatePicker() {
        val onlyDate = LocalDate.now()
        binding.albumShowDateEt.text = onlyDate.toString()

        binding.albumShowDateEt.setOnClickListener {
            // 현재 설정되어있는 날짜를 넘겨줌
            val dateDialog = DialogDatePickerDark(binding.albumShowDateEt.text.toString())
            dateDialog.show(supportFragmentManager, "dateDialogDark")

            // DialogDatePicker의 날짜 변경 인터페이스를 불러와서 TextView에 날짜를 저장
            dateDialog.setMyItemClickListener(object  : DialogDatePickerDark.MyItemClickListener {
                override fun saveShow(date: String) {
                    binding.albumShowDateEt.text = date
                }
            })
        }
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
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 80.toPx() - binding.albumMakeSaveBt.height / 2)
        toast.show()
    }

    private fun initAlbum() {
        album.albumImgUrl = imgUrl
        album.albumTitle = "${binding.albumMakeTitleEt.text.trim()}"
        album.albumDate = "${binding.albumShowDateEt.text.trim()}"
        album.userIdx = getUserIdx()
        album.bandIdx = bandIdx!!
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onSendSuccess(result: String) {
        Log.d("SENDIMG/SUC", result)
        imgUrl = result
    }

    private fun getUserIdx(): Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMG/FAIL", "$code $message")
    }

    override fun onMakeSuccess(result: MakeAlbumBandResult) {
        Log.d("MAKE/SUC", result.toString())
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 100)
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE/FAIL", "$code $message")
        setToast(message)
    }
}