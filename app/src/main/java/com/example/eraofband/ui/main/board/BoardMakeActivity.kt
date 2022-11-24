package com.example.eraofband.ui.main.board

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Board
import com.example.eraofband.data.PostImgUrl
import com.example.eraofband.databinding.ActivityBoardMakeBinding
import com.example.eraofband.remote.board.postBoard.PostBoardService
import com.example.eraofband.remote.board.postBoard.PostBoardView
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import com.example.eraofband.ui.setOnSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class BoardMakeActivity : AppCompatActivity(), PostBoardView, SendImgView {

    private lateinit var binding: ActivityBoardMakeBinding
    private var postImgUrl = arrayListOf<PostImgUrl>()
    private var board = Board(0, "", postImgUrl, "", 0)
    private var category = -1

    private lateinit var getResult: ActivityResultLauncher<Intent> //사진 가져오는 함수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoardMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.boardMakeBackIb.setOnClickListener { finish() }

        binding.boardMakeRegisterBtn.setOnSingleClickListener {
            postBoard()
            val postBoardService = PostBoardService()
            postBoardService.setBoardView(this)
            postBoardService.postBoard(getJwt()!!, board)
            Log.d("imgTest", board.toString())
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 100)
        }

        getImage()
        binding.boardMakePictureIv.setOnSingleClickListener {
            initImageView()
        }
        initTopicSpinner()
    }

    private fun postBoard() {
        board.category = category
        board.title = binding.boardMakeTitleEt.text.toString()
        board.content = binding.boardMakeContentEt.text.toString()
        board.userIdx = getUserIdx()
        board.postImgsUrl = postImgUrl
    }

    private fun initRVAdapter(item: List<PostImgUrl>) {
        val boardImgRVAdapter = BoardImgRVAdapter()
        if (postImgUrl.size >= 5) { // 사진이 5개 이상이면 추가 X
            binding.boardMakePictureIv.visibility = View.GONE
        } else {
            binding.boardMakePictureIv.visibility = View.VISIBLE
        }
        binding.boardMakePictureRv.adapter = boardImgRVAdapter
        binding.boardMakePictureRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        boardImgRVAdapter.initImg(item)
        boardImgRVAdapter.setMyItemClickListener(object : BoardImgRVAdapter.MyItemClickListener{

            override fun onDelete(position: Int) {
                postImgUrl.removeAt(position)
                initRVAdapter(postImgUrl)
            }
        })
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

    private fun initImageView() {
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
                } else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
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

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    override fun onPostSuccess(result: String) {
        Log.d("POST BOARD / SUC", result)
    }

    override fun onPostFailure(code: Int, message: String) {
        Log.d("POST BOARD / FAIL", "$code $message")
        Toast.makeText(baseContext, "$message", Toast.LENGTH_SHORT).show()
    }

    override fun onSendSuccess(result: String) {
        postImgUrl.add(PostImgUrl(result))
        initRVAdapter(postImgUrl)
        Log.d("SENDING / SUC", result)
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDING / FAIL", "$code $message")
    }
}