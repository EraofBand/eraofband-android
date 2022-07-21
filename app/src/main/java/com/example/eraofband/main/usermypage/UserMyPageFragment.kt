package com.example.eraofband.main.usermypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityUserMypageBinding
import com.example.eraofband.main.MainActivity
import com.example.eraofband.main.mypage.follow.FollowActivity
import com.example.eraofband.remote.getMyPage.GetMyPageService
import com.example.eraofband.remote.getotheruser.GetOtherUserResult
import com.example.eraofband.remote.getotheruser.GetOtherUserService
import com.example.eraofband.remote.getotheruser.GetOtherUserView
import com.example.eraofband.remote.userfollow.UserFollowResponse
import com.example.eraofband.remote.userfollow.UserFollowService
import com.example.eraofband.remote.userfollow.UserFollowView
import com.example.eraofband.remote.userunfollow.UserUnfollowResponse
import com.example.eraofband.remote.userunfollow.UserUnfollowService
import com.example.eraofband.remote.userunfollow.UserUnfollowView
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class UserMyPageActivity : AppCompatActivity(), GetOtherUserView, UserFollowView, UserUnfollowView {

    private lateinit var binding: ActivityUserMypageBinding
    private var otherUserIdx : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMypageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val userMyPageAdapter = UserMyPageVPAdapter(this)
        binding.userMypageVp.adapter = userMyPageAdapter

        TabLayoutMediator(binding.userMypageTb, binding.userMypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
            }
        }.attach()

        binding.userMypageFollowTv.setOnClickListener {                 // 팔로우 리스트에서 언팔 및 팔로우 시 visibility 변경
            binding.userMypageFollowTv.visibility = View.INVISIBLE
            binding.userMypageUnfollowTv.visibility = View.VISIBLE
            val userFollowService = UserFollowService() // 팔로우
            userFollowService.setUserFollowView(this)
            userFollowService.userFollow(getJwt()!!, otherUserIdx!!)
        }

        binding.userMypageUnfollowTv.setOnClickListener {
            binding.userMypageFollowTv.visibility = View.VISIBLE
            binding.userMypageUnfollowTv.visibility = View.INVISIBLE
            val userUnfollowService = UserUnfollowService() // 언팔로우
            userUnfollowService.setUserUnfollowView(this)
            userUnfollowService.userUnfollow(getJwt()!!, otherUserIdx!!)
        }
        moveFollowActivity()
    }

    override fun onStart() {
        super.onStart()
        val getOtherUserService = GetOtherUserService()
        getOtherUserService.setOtherUserView(this)
        getOtherUserService.getOtherUser(getJwt()!!, 82)
    }

    private fun moveFollowActivity() {
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

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
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

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetOtherUserResult) {
        // 나중에 프사도 연동 예정, 포트폴리오는 아직

        Log.d("MYPAGE", result.toString())
        binding.userMypageNicknameTv.text = result.getUser.nickName
        binding.userMypageInfoNicknameTv.text = result.getUser.nickName // 닉네임 연동

        otherUserIdx = result.getUser.userIdx // 해당 유저 인덱스

        // 디테일한 소개 연동

        val index = result.getUser.region.split(" ")
        val city = index[1]

        val age = setDate().substring(0, 4).toInt() - result.getUser.birth.substring(0, 4).toInt() + 1

        val gender =
            if(result.getUser.gender == "MALE") "남성"
            else "여성"

        binding.userMypageDetailInfoTv.text = "$city | ${age}세 | $gender"

        binding.userMypageIntroductionTv.text = result.getUser.introduction  // 내 소개 연동

        if (binding.userMypageIntroductionTv.lineCount > 3) {
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

        setSession(result.getUser.mySession)  // 세션 연동
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("USER MYPAGE / FAIL", "$code $message")
    }

    // 팔로우 리스폰스----------------------------------------------------------------
    override fun onUserFollowSuccess(code: Int, response: UserFollowResponse) {
        Log.d("USER FOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserFollowFailure(code: Int, message: String) {
        Log.d("USER FOLLOW / FAIL", "$code $message")
    }

    // 언팔로우 리스폰스-------------------------------------------------------------------
    override fun onUserUnfollowSuccess(code: Int, response: UserUnfollowResponse) {
        Log.d("USER UNFOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserUnfollowFailure(code: Int, message: String) {
        Log.d("USER UNLLOW / FAIL", "$code $message")
    }
}