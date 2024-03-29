package com.example.eraofband.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityMainBinding
import com.example.eraofband.ui.main.board.BoardFragment
import com.example.eraofband.ui.main.chat.ChatFragment
import com.example.eraofband.ui.main.community.CommunityFragment
import com.example.eraofband.ui.main.community.CommunityInterface
import com.example.eraofband.ui.main.home.HomeFragment
import com.example.eraofband.ui.main.mypage.MyPageFragment
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var communityInterface: CommunityInterface

    private var boardFragment: BoardFragment? = null
    private var communityFragment: CommunityFragment? = null
    private var homeFragment: HomeFragment? = null
    private var chatFragment: ChatFragment? = null
    private var myPageFragment: MyPageFragment? = null
    private var nowCommunity = false

    private var waitTime = 0L

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

        Log.d("USERIDX", "${getUserIdx()} ${getUserJwt()}")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getUserJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    fun setUserView(cf: CommunityFragment) {
        communityFragment = cf
        communityInterface = communityFragment as CommunityFragment
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - waitTime >= 1000 ) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish() // 액티비티 종료
        }
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
            nowCommunity = false
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

                    if (nowCommunity) {
                        communityInterface.refresh()
                    }
                    nowCommunity = true
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
                    nowCommunity = false
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
                    nowCommunity = false
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
                    nowCommunity = false
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}