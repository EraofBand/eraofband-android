package com.example.eraofband.main.usermypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityUserMypageBinding
import com.example.eraofband.main.mypage.follow.FollowActivity
import com.example.eraofband.remote.getotheruser.GetOtherUserResult
import com.example.eraofband.remote.getotheruser.GetOtherUserService
import com.example.eraofband.remote.getotheruser.GetOtherUserView
import com.example.eraofband.remote.portfolio.PofolCommentResult
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class UserMyPageActivity : AppCompatActivity(), GetOtherUserView {

    private lateinit var binding: ActivityUserMypageBinding
    private var userIdx = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityUserMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        userIdx = intent.extras?.getInt("comment")!!
        Log.d("USER INDEX", userIdx.toString())

        val userMyPageAdapter = UserMyPageVPAdapter(this)
        binding.userMypageVp.adapter = userMyPageAdapter

        TabLayoutMediator(binding.userMypageTb, binding.userMypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
            }
        }.attach()

        binding.userMypageFollowing.setOnClickListener {
            var intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("current", 0)
            startActivity(intent)
        }

        binding.userMypageFollower.setOnClickListener {
            var intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("current", 1)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val getOtherUserService = GetOtherUserService()

        getOtherUserService.setOtherUserView(this)
        getOtherUserService.getOtherUser(getJwt()!!, userIdx)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }


    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetOtherUserResult) {
        // 나중에 프사도 연동 예정, 포트폴리오는 아직

        Log.d("MYPAGE", result.toString())
        binding.userMypageNicknameTv.text = result.getUser.nickName
        binding.userMypageInfoNicknameTv.text = result.getUser.nickName // 닉네임 연동

        // 디테일한 소개 연동

        val index = result.getUser.region.split(" ")
        val city = index[1]

        val age = setDate().substring(0, 4).toInt() - result.getUser.birth.substring(0, 4).toInt() + 1

        val gender =
            if(result.getUser.gender == "MALE") "남성"
            else "여성"

        binding.userMypageDetailInfoTv.text = "$city | ${age}세 | $gender"

        binding.userMypageIntroductionTv.text = result.getUser.introduction  // 내 소개 연동

        if(binding.userMypageIntroductionTv.lineCount > 3) {
            binding.userMypageLookMoreTv.visibility = View.VISIBLE  // 더보기 표시

            // 더보기 클릭 이벤트
            binding.userMypageLookMoreTv.setOnClickListener {
                if (binding.userMypageLookMoreTv.text == "더보기") {
                    binding.userMypageLookMoreTv.text = "접기"
                    binding.userMypageIntroductionTv.maxLines = 100
                }
                else {
                    binding.userMypageLookMoreTv.text = "더보기"
                    binding.userMypageIntroductionTv.maxLines = 3
                }
            }
        }

        // 숫자 연동
        binding.userMypageFollowingCntTv.text = result.getUser.followeeCount.toString()
        binding.userMypageFollowerCntTv.text = result.getUser.followerCount.toString()
        binding.userMypagePortfolioCntTv.text = result.getUser.pofolCount.toString()

        setSession(result.getUser.userSession)  // 세션 연동
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("USER MYPAGE / FAIL", "$code $message")
    }


    @SuppressLint("SimpleDateFormat")
    private fun setDate() : String {  // 오늘 날짜 불러오기
        val today = System.currentTimeMillis()  // 현재 날짜, 시각 불러오기
        val date = Date(today)
        val mFormat = SimpleDateFormat("yyyy-MM-dd")

        return mFormat.format(date)
    }

    private fun setSession(session : Int) {
        when (session) {
            0 -> binding.userMypageSessionTv.text = "보컬"
            1 -> binding.userMypageSessionTv.text = "기타"
            2 -> binding.userMypageSessionTv.text = "베이스"
            3 -> binding.userMypageSessionTv.text = "드럼"
            else ->  binding.userMypageSessionTv.text = "키보드"
        }
    }
}