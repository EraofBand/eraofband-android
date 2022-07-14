package com.example.eraofband.main.mypage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityMypageSettingBinding
import com.example.eraofband.login.LoginActivity
import com.kakao.sdk.user.UserApiClient


class MyPageSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBackIb.setOnClickListener{ finish() }  // 뒤로가기

        logout()
        resign()

    }


    private fun logout() {
        binding.settingLogoutCl.setOnClickListener {  // 로그아웃 프로세스
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    removeToken()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()  // 로그아웃시 스택에 있는 메인 액티비티 종료
                }
            }
        }
    }

    private fun resign() {
        binding.settingRemoveCl.setOnClickListener {  // 회원탈퇴 프로세스
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
                    removeToken()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()  // 로그아웃시 스택에 있는 메인 액티비티 종료
                }
            }
        }
    }

    private fun removeToken() {
        // 현재 로그인 한 사람의 ACCESS-TOKEN 정보 지우기
        val tokenSP = getSharedPreferences("token", MODE_PRIVATE)
        val tokenEdit = tokenSP.edit()

        tokenEdit.remove("tokenInfo")
        tokenEdit.apply()
    }
}