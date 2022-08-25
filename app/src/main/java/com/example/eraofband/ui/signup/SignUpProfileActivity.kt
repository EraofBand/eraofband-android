package com.example.eraofband.ui.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ActivitySignupProfileBinding
import com.example.eraofband.remote.sendimg.SendImgService
import com.example.eraofband.remote.sendimg.SendImgView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SignUpProfileActivity : AppCompatActivity(), SendImgView {

    private lateinit var binding: ActivitySignupProfileBinding
    private var user = User("", "", "", "", "", "", 0)
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = intent
        user = intent.extras?.getSerializable("user") as User

        intent = Intent(this, SignUpLocationActivity::class.java)

        binding.signupProfileNextBtn.setOnClickListener {

            if(binding.signupProfileAddIv.visibility == View.VISIBLE) {
                setToast()
            }
            else {
                intent.putExtra("user", user)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
            }
        }

        binding.signupProfileBackIv.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_back, R.anim.slide_right_back)
        }

        setTextColor()
        getImage()

        binding.signupProfileAddIv.setOnClickListener{
            initImageViewProfile()
        }
        binding.signupProfileProfileIv.setOnClickListener{
            initImageViewProfile()
        }
    }

    private fun setTextColor() {
        // 글씨 파란색, 두껍게 만들기
        val nickname = binding.signupProfileTitleTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(nickname)  //객체 생성

        val word = "프로필 사진"
        val start = nickname.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupProfileTitleTv.text = spannableString
    }

    private fun setToast() {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = "프로필 사진을 추가해주세요!"

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기
        val height = size.y / 2  // 토스트 메세지가 중간에 고정되어있기 때문에 높이 / 2

        // 중간부터 marginBottom, 버튼 높이 / 2 만큼 빼줌
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 80.toPx() - binding.signupProfileNextBtn.height / 2)
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

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
                // 이미지 가져오기 성공하면 원래 이미지를 없애고 가져온 사진을 넣어줌
                // 이미지 동그랗게 + CenterCrop
                if (selectedImageUri != null) {
                    Glide.with(this).load(selectedImageUri)
                        .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                        .into(binding.signupProfileProfileIv)

                    val imgPath = absolutelyPath(selectedImageUri, this)
                    val file = File(imgPath)
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    val sendImgService = SendImgService()
                    sendImgService.setImageView(this)
                    sendImgService.sendImg(body)

                    binding.signupProfileProfileIv.setBackgroundResource(R.drawable.profile_border)
                    binding.signupProfileAddIv.visibility = View.GONE
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun absolutelyPath(path: Uri?, context : Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        val result = c?.getString(index!!)

        return result!!
    }

    override fun onSendSuccess(result: String) {
        Log.d("SENDIMGss", result)
        user.profileImgUrl = result
    }

    override fun onSendFailure(code: Int, message: String) {
        Log.d("SENDIMGss", "$code $message")
    }
}