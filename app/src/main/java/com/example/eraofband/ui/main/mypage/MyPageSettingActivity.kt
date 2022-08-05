package com.example.eraofband.ui.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityMypageSettingBinding
import com.example.eraofband.remote.user.signout.*
import com.example.eraofband.ui.login.LoginActivity
import com.kakao.sdk.user.UserApiClient


class MyPageSettingActivity : AppCompatActivity(), ResignView {

    private lateinit var binding: ActivityMypageSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBackIb.setOnClickListener{ finish() }  // 뒤로가기

        binding.settingLogoutCl.setOnClickListener {  // 로그아웃 프로세스
            val resignDialog = ResignDialog(0)
            resignDialog.show(this.supportFragmentManager, "logout")

            resignDialog.setDialogListener(object : ResignDialog.ResignListener {
                override fun resign() {
                    resignStart()
                }

                override fun logout() {
                    logoutStart()
                }
            })
        }

        binding.settingRemoveCl.setOnClickListener {  // 회원탈퇴 프로세스
            val resignDialog = ResignDialog(1)
            resignDialog.show(this.supportFragmentManager, "resign")

            resignDialog.setDialogListener(object : ResignDialog.ResignListener {
                override fun resign() {
                    resignStart()
                }

                override fun logout() {
                    logoutStart()
                }
            })
        }
    }


    private fun logoutStart() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                removeUser()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()  // 로그아웃시 스택에 있는 메인 액티비티 종료
            }
        }
    }

    private fun resignStart() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Toast.makeText(this, "회원탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()

                // DB에서 지우기
                val resignService = ResignService()
                resignService.setResignView(this)
                resignService.resign(getJwt()!!, getUserIdx())

                removeUser()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()  // 회원탈퇴시 스택에 있는 메인 액티비티 종료
            }
        }
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun removeUser() {
        // 현재 로그인 한 사람의 정보 지우기
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        val userEdit = userSP.edit()

        userEdit.remove("token")
        userEdit.remove("userIdx")
        userEdit.remove("jwt")

        userEdit.apply()
    }

    override fun onResignSuccess(code: Int, response: ResignResponse) {
        Log.d("RESIGN/SUC", response.toString())
    }

    override fun onResignFailure(code: Int, message: String) {
        Log.d("RESIGN/FAIL", "$code $message")
    }
}