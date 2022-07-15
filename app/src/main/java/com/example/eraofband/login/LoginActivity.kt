package com.example.eraofband.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityLoginBinding
import com.example.eraofband.main.MainActivity
import com.example.eraofband.remote.checkUser.CheckUserResult
import com.example.eraofband.remote.checkUser.CheckUserService
import com.example.eraofband.remote.checkUser.CheckUserView
import com.example.eraofband.signup.SignUpNicknameActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), CheckUserView {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                // 토큰 정보 임시 저장
                val userSP = getSharedPreferences("user", MODE_PRIVATE)
                val userEdit = userSP.edit()

                userEdit.putString("token", token.accessToken)
                userEdit.apply()
                Log.d("token",token.accessToken)

                // 이메일 불러와서 DB에 유저가 있는지 확인
                UserApiClient.instance.me { user, _ ->
                    if(user != null) {
                        val email = user.kakaoAccount?.email.toString()

                        Log.d("EMAIL", email)

                        val checkUserService = CheckUserService()
                        checkUserService.setUserView(this)
                        checkUserService.checkUser(email)
                    }
                }
            }
        }

        binding.loginKakaoBt.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    override fun onCheckSuccess(message: String, result: CheckUserResult) {
        // DB에 유저가 있으면 정보 저장 후 메인으로
        Log.d("CHECK/SUCCESS", result.toString())

        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        val userEdit = userSP.edit()

        if (result.jwt == "NULL" && result.userIdx == 0) {  // DB에 유저가 없는 case
            Toast.makeText(applicationContext,"처음 가입한 유저입니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignUpNicknameActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

        } else {  // DB에 유저가 있는 case
            Toast.makeText(
                applicationContext,
                "가입한 유저입니다. userIdx = ${result.userIdx}",
                Toast.LENGTH_SHORT
            ).show()
            userEdit.putInt("userIdx", result.userIdx)
            userEdit.putString("jwt", result.jwt)
            userEdit.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

    override fun onCheckFailure(code: Int, message: String) {
        Log.d("CHECK/FAIL", "$code $message")
    }
}
