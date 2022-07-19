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
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var boardFragment: BoardFragment? = null
    private var communityFragment: CommunityFragment? = null
    private var homeFragment: HomeFragment? = null
    private var chatFragment: ChatFragment? = null
    private var myPageFragment: MyPageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀네비
        initBottomNav()

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

        Log.d("USERIDX", "${getUserIdx()}")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun initBottomNav(){
        // 시작 화면은 홈 화면으로 설정
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_frm, homeFragment!!).commitAllowingStateLoss()
        binding.mainBottomNav.selectedItemId = R.id.home

        // 중앙 버튼을 누르면 HomeFragment 실행
        binding.mainBottomHomeBt.setOnClickListener {
            if(homeFragment != null)  supportFragmentManager.beginTransaction().show(homeFragment!!).commitAllowingStateLoss()
            if(communityFragment != null)  supportFragmentManager.beginTransaction().hide(communityFragment!!).commitAllowingStateLoss()
            if(boardFragment != null)  supportFragmentManager.beginTransaction().hide(boardFragment!!).commitAllowingStateLoss()
            if(chatFragment != null)  supportFragmentManager.beginTransaction().hide(chatFragment!!).commitAllowingStateLoss()
            if(myPageFragment != null)  supportFragmentManager.beginTransaction().hide(myPageFragment!!).commitAllowingStateLoss()

            binding.mainBottomNav.setBackgroundResource(R.drawable.bottom_nav_bg)
            binding.mainBottomNav.selectedItemId = R.id.home
        }

        binding.mainBottomNav.setOnItemSelectedListener{
                item ->
            when (item.itemId) {

                R.id.community -> {  // 소셜 화면 실행
                    if(communityFragment == null){
                        communityFragment = CommunityFragment()
                        supportFragmentManager.beginTransaction().add(R.id.main_frm, communityFragment!!).commitAllowingStateLoss()
                    }

                    if(homeFragment != null)  supportFragmentManager.beginTransaction().hide(homeFragment!!).commitAllowingStateLoss()
                    if(communityFragment != null)  supportFragmentManager.beginTransaction().show(communityFragment!!).commitAllowingStateLoss()
                    if(boardFragment != null)  supportFragmentManager.beginTransaction().hide(boardFragment!!).commitAllowingStateLoss()
                    if(chatFragment != null)  supportFragmentManager.beginTransaction().hide(chatFragment!!).commitAllowingStateLoss()
                    if(myPageFragment != null)  supportFragmentManager.beginTransaction().hide(myPageFragment!!).commitAllowingStateLoss()

                    binding.mainBottomNav.setBackgroundResource(R.drawable.bottom_nav_bg)
                    return@setOnItemSelectedListener true
                }

                R.id.board -> {  // 게시판 화면 실행
                    if(boardFragment == null){
                        boardFragment = BoardFragment()
                        supportFragmentManager.beginTransaction().add(R.id.main_frm,boardFragment!!).commitAllowingStateLoss()
                    }

                    if(homeFragment != null)  supportFragmentManager.beginTransaction().hide(homeFragment!!).commitAllowingStateLoss()
                    if(communityFragment != null)  supportFragmentManager.beginTransaction().hide(communityFragment!!).commitAllowingStateLoss()
                    if(boardFragment != null)  supportFragmentManager.beginTransaction().show(boardFragment!!).commitAllowingStateLoss()
                    if(chatFragment != null)  supportFragmentManager.beginTransaction().hide(chatFragment!!).commitAllowingStateLoss()
                    if(myPageFragment != null)  supportFragmentManager.beginTransaction().hide(myPageFragment!!).commitAllowingStateLoss()

                    binding.mainBottomNav.setBackgroundResource(R.drawable.bottom_nav_bg)
                    return@setOnItemSelectedListener true
                }

                R.id.chat -> {  // 채팅 화면 실행
                    if(chatFragment == null){
                        chatFragment = ChatFragment()
                        supportFragmentManager.beginTransaction().add(R.id.main_frm, chatFragment!!).commitAllowingStateLoss()
                    }

                    if(homeFragment != null)  supportFragmentManager.beginTransaction().hide(homeFragment!!).commitAllowingStateLoss()
                    if(communityFragment != null)  supportFragmentManager.beginTransaction().hide(communityFragment!!).commitAllowingStateLoss()
                    if(boardFragment != null)  supportFragmentManager.beginTransaction().hide(boardFragment!!).commitAllowingStateLoss()
                    if(chatFragment != null)  supportFragmentManager.beginTransaction().show(chatFragment!!).commitAllowingStateLoss()
                    if(myPageFragment != null)  supportFragmentManager.beginTransaction().hide(myPageFragment!!).commitAllowingStateLoss()


                    binding.mainBottomNav.setBackgroundResource(R.drawable.bottom_nav_bg)
                    return@setOnItemSelectedListener true
                }

                R.id.mypage -> {  // 프로필 화면 실행
                    if(myPageFragment == null){
                        myPageFragment = MyPageFragment()
                        supportFragmentManager.beginTransaction().add(R.id.main_frm, myPageFragment!!).commitAllowingStateLoss()
                    }

                    if(homeFragment != null)  supportFragmentManager.beginTransaction().hide(homeFragment!!).commitAllowingStateLoss()
                    if(communityFragment != null)  supportFragmentManager.beginTransaction().hide(communityFragment!!).commitAllowingStateLoss()
                    if(boardFragment != null)  supportFragmentManager.beginTransaction().hide(boardFragment!!).commitAllowingStateLoss()
                    if(chatFragment != null)  supportFragmentManager.beginTransaction().hide(chatFragment!!).commitAllowingStateLoss()
                    if(myPageFragment != null)  supportFragmentManager.beginTransaction().show(myPageFragment!!).commitAllowingStateLoss()

                    binding.mainBottomNav.setBackgroundResource(R.drawable.bottom_nav_bg_none)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}