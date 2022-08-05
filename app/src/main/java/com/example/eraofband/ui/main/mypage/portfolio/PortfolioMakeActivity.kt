package com.example.eraofband.ui.main.mypage.portfolio

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityPortfolioMakeBinding
import com.example.eraofband.remote.portfolio.makePofol.MakePofolResult
import com.example.eraofband.remote.portfolio.makePofol.MakePofolService
import com.example.eraofband.remote.portfolio.makePofol.MakePofolView
import com.example.eraofband.remote.sendimg.SendImgResponse
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class PortfolioMakeActivity : AppCompatActivity(), SendImgView, MakePofolView {

    private lateinit var binding: ActivityPortfolioMakeBinding
    private var videoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioMakeBackIb.setOnClickListener { finish() }  // 뒤로가기

        // 비디오 올리기 혹은 올린 썸네일을 누르면 갤러리에 들어갈 수 있도록 해줌 (조은아 내가 그냥 레이아웃 클릭하면 갤러리로 바꿨어)
        binding.portfolioMakeVideoCl.setOnClickListener { initImageViewProfile() }

        binding.portfolioMakeSaveBt.setOnClickListener {  // 포트폴리오 올리기
            if(binding.portfolioMakeTitleEt.text.isNotEmpty() && binding.portfolioMakeVideoIntroEt.text.isNotEmpty() && videoUrl != "") {
                // 제목, 소개, 비디오를 모두 올린 경우 정보 저장 후 포트폴리오 올려줌
                val title = binding.portfolioMakeTitleEt.text.toString()
                val content = binding.portfolioMakeVideoIntroEt.text.toString()

                val makeService = MakePofolService()
                makeService.setMakeView(this)
                makeService.makePortfolio(getJwt()!!, Portfolio(content, videoUrl, title, getUserIdx(), videoUrl))

                finish()
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

    private fun getProfileUrl(): String? {
        val profileSp = getSharedPreferences("profile", MODE_PRIVATE)
        return profileSp.getString("url", "")
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
        // 가져올 컨텐츠들 중에서 video 만을 가져온다.
        intent.type = "video/*"
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
                val selectedVideoUri: Uri? = data?.data
                // 이미지 가져오기 성공하면 원래 이미지를 없애고 가져온 사진을 넣어줌
                if (selectedVideoUri != null) {
                    // 썸네일 띄우기
                    binding.portfolioThumbnailIv.visibility = View.VISIBLE
                    binding.portfolioThumbnailIv.clipToOutline = true
                    binding.portfolioThumbnailIv.setImageBitmap(createThumbnail(this, selectedVideoUri))

                    // 절대 주소 받아오기
                    val videoPath = absolutelyPath(selectedVideoUri, this)
                    val file = File(videoPath)
                    val requestBody = RequestBody.create(MediaType.parse("video/*"), file)
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
            .setMessage("동영상을 가져오기 위해서는 갤러리 접근 권한이 필요합니다.")
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

    override fun onSendSuccess(response: SendImgResponse) {
        Log.d("SENDIMG/SUC", response.toString())
        videoUrl = response.result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMG/FAIL", "$code $message")
    }

    private fun createThumbnail(context: Context, path: Uri): Bitmap? {
        var retriever: MediaMetadataRetriever? = null
        var bitmap: Bitmap? = null

        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, path)

            bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            retriever?.release()
        }

        return bitmap
    }

    override fun onMakeSuccess(code: Int, result: MakePofolResult) {
        Log.d("MAKE/SUC", result.toString())

        finish()  // 올리기 성공하면 창 닫기
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE/FAIL", "$code $message")
    }
}