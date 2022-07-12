package com.example.eraofband.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityMainBinding
import com.example.eraofband.main.board.BoardFragment
import com.example.eraofband.main.chat.ChatFragment
import com.example.eraofband.main.community.CommunityFragment
import com.example.eraofband.main.home.HomeFragment
import com.example.eraofband.main.mypage.MyPageFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.kakao.sdk.user.UserApiClient

//test commit
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀네비
        initBottomNav()

        // 바텀 네비게이션 모서리 둥글게 만들기
        val bottomNavBg = binding.mainBottomNav.background as MaterialShapeDrawable
        bottomNavBg.shapeAppearanceModel = bottomNavBg.shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 70.0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 70.0f).build()

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("kakaoinfo", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("kakaoinfo", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }

    private fun initBottomNav(){
        // 시작 화면은 홈 화면으로 설정
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        binding.mainBottomNav.selectedItemId = R.id.home

        // 중앙 버튼을 누르면 HomeFragment 실행
        binding.mainBottomHomeBt.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
            binding.mainBottomNav.selectedItemId = R.id.home
        }

        binding.mainBottomNav.setOnItemSelectedListener{
                item ->
            when (item.itemId) {

                R.id.community -> {  // 소셜 화면 실행
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, CommunityFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.board -> {  // 게시판 화면 실행
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, BoardFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.chat -> {  // 채팅 화면 실행
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChatFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mypage -> {  // 프로필 화면 실행
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyPageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}