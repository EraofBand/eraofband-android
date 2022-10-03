package com.example.eraofband.ui.main.mypage.portfolio

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityPortfolioMakeBinding
import com.example.eraofband.remote.portfolio.makePofol.MakePofolResult
import com.example.eraofband.remote.portfolio.makePofol.MakePofolService
import com.example.eraofband.remote.portfolio.makePofol.MakePofolView
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import com.example.eraofband.ui.setOnSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class PortfolioMakeActivity : AppCompatActivity(), SendImgView, MakePofolView {

    private lateinit var binding: ActivityPortfolioMakeBinding

    private val sendImgService = SendImgService()

    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var videoPath: Uri
    private var videoUrl = ""

    private var thumbnail = false
    private var thumbnailUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sendImgService.setImageView(this)

        val makeService = MakePofolService()
        makeService.setMakeView(this)

        binding.portfolioMakeBackIb.setOnClickListener { finish() }  // 뒤로가기

        // 비디오 올리기 혹은 올린 썸네일을 누르면 갤러리에 들어갈 수 있도록 해줌 (조은아 내가 그냥 레이아웃 클릭하면 갤러리로 바꿨어)
        getVideo()
        binding.portfolioMakeVideoCl.setOnSingleClickListener { initImageViewProfile() }

        binding.portfolioMakeSaveBt.setOnSingleClickListener {  // 포트폴리오 올리기
            val title = "${binding.portfolioMakeTitleEt.text.trim()}"
            val content = "${binding.portfolioMakeVideoIntroEt.text.trim()}"

            if(title.isNotEmpty() && content.isNotEmpty() && videoUrl != "" && thumbnailUrl != "") {
                // 제목, 소개, 비디오를 모두 올린 경우 정보 저장 후 포트폴리오 올려줌
                makeService.makePortfolio(getJwt()!!, Portfolio(content, thumbnailUrl, title, getUserIdx(), videoUrl))

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 100)
            }
        }
    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx(): Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
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

    private fun initImageViewProfile() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
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
                    intent.type = "video/*"
                    getResult.launch(intent)
                }
                else Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                //
            }
        }
    }

    private fun getVideo() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("RESULT", result.data.toString())
                val selectedVideoUri: Uri? = result.data?.data
                if (selectedVideoUri != null) {

                    // 썸네일 띄우기
                    thumbnail = true
                    videoPath = selectedVideoUri
                    createThumbnail(this, selectedVideoUri)

                } else {
                    Toast.makeText(this, "비디오를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "비디오를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
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

    private fun sendImage(selectedVideoUri: Uri) {
        // 절대 주소 받아오기
        val videoPath = absolutelyPath(selectedVideoUri, this)
        val file = File(videoPath)
        val requestBody = RequestBody.create(MediaType.parse("video/*"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

        sendImgService.sendImg(body)
    }

    private fun createThumbnail(context: Context, path: Uri): Bitmap? {
        var retriever: MediaMetadataRetriever? = null
        var bitmap: Bitmap? = null

        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, path)

            bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

            getImageUri(bitmap!!)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            retriever?.release()
        }

        return bitmap
    }

    private fun getImageUri(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "uri", null)

        sendImage(path.toUri())
    }

    override fun onSendSuccess(result: String) {
        Log.d("SENDIMG/SUC", result)
        if(thumbnail) {  // 썸네일
            Log.d("THUMBNAIL", result)
            binding.portfolioThumbnailIv.visibility = View.VISIBLE
            binding.portfolioThumbnailIv.clipToOutline = true
            Glide.with(this).load(result).into(binding.portfolioThumbnailIv)

            thumbnailUrl = result
            thumbnail = false

            sendImage(videoPath)  // 비디오
        }
        else {
            Log.d("VIDEO", result)
            videoUrl = result
        }
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMG/FAIL", "$code $message")
    }

    override fun onMakeSuccess(code: Int, result: MakePofolResult) {
        Log.d("MAKE/SUC", result.toString())

        finish()  // 올리기 성공하면 창 닫기
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE/FAIL", "$code $message")
    }
}