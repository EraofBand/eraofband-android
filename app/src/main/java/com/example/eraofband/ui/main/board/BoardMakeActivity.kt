package com.example.eraofband.ui.main.board

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eraofband.R
import com.example.eraofband.data.Board
import com.example.eraofband.data.PostImgUrl
import com.example.eraofband.databinding.ActivityBoardMakeBinding
import com.example.eraofband.remote.board.postBoard.PostBoardResult
import com.example.eraofband.remote.board.postBoard.PostBoardService
import com.example.eraofband.remote.board.postBoard.PostBoardView
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class BoardMakeActivity : AppCompatActivity(), PostBoardView, SendImgView {

    private lateinit var binding: ActivityBoardMakeBinding
    private var postImgsUrl = arrayListOf<PostImgUrl>()
    private var board = Board(0, "", postImgsUrl, "", 0)

    private var category = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoardMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postImgsUrl.add(PostImgUrl(""))
        binding.boardMakeBackIb.setOnClickListener { finish() }

        binding.boardMakeRegisterBtn.setOnClickListener {
            postBoard()
            val postBoardService = PostBoardService()
            postBoardService.setBoardView(this)
            postBoardService.postBoard(getJwt()!!, board)
            Log.d("board",board.toString())
        }
        initTopicSpinner()
    }

    private fun postBoard() {
        board.category = category
        board.title = binding.boardMakeTitleEt.text.toString()
        board.content = binding.boardMakeContentEt.text.toString()
        board.userIdx = getUserIdx()
        board.postImgsUrl = postImgsUrl
    }


    private fun initTopicSpinner() {
        //  게시판 주제 스피너
        val lesson = resources.getStringArray(R.array.board)

        val boardAdapter = ArrayAdapter(this, R.layout.item_spinner, lesson)
        binding.boardMakeTopicSp.adapter = boardAdapter
        binding.boardMakeTopicSp.setSelection(0)

        binding.boardMakeTopicSp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                when(position) {
                    0 -> category = 0
                    1 -> category = 1
                    2 -> category = 2
                    3 -> category = 3
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                category = 0
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
                        //.into(binding.)

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

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    override fun onPostSuccess(code: Int, result: PostBoardResult) {
        Log.d("POST BOARD / SUC", result.toString())
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 100)
    }

    override fun onPostFailure(code: Int, message: String) {
        Log.d("POST BOARD / FAIL", "$code $message")
    }

    override fun onSendSuccess(result: String) {
        TODO("Not yet implemented")
    }

    override fun onSendFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}