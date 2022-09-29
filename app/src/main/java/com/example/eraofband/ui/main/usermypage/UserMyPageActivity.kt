package com.example.eraofband.ui.main.usermypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityUserMypageBinding
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserResult
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserService
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserView
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollow.UserFollowService
import com.example.eraofband.remote.user.userFollow.UserFollowView
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowResponse
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowService
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowView
import com.example.eraofband.ui.main.chat.ChatContentActivity
import com.example.eraofband.ui.main.mypage.follow.FollowActivity
import com.example.eraofband.ui.report.ReportDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class UserMyPageActivity : AppCompatActivity(), GetOtherUserView, UserFollowView, UserUnfollowView {

    private lateinit var binding: ActivityUserMypageBinding
    internal var otherUserIdx: Int? = null
    private var followerCnt = 0

    private lateinit var nickName: String
    private lateinit var profileImg: String
    private var secondIndex = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        otherUserIdx = intent.extras?.getInt("userIdx")!!
        Log.d("USER INDEX", otherUserIdx.toString())

    }

    override fun onResume() {
        super.onResume()

        val getOtherUserService = GetOtherUserService()
        getOtherUserService.setOtherUserView(this)
        getOtherUserService.getOtherUser(getJwt()!!, otherUserIdx!!)

        clickListener()
    }

    private fun clickListener() {

        binding.userMypageBackIb.setOnClickListener {
            finish()
        }
        binding.userMypageMoreIv.setOnClickListener { showPopup() }

        // 메세지 클릭하면 채팅방으로 이동
        binding.userMypageMessageTv.setOnClickListener {
            val intent = Intent(this, ChatContentActivity::class.java)
            intent.putExtra("nickname", nickName)
            intent.putExtra("profile", profileImg)
            intent.putExtra("firstIndex", getUserIdx())
            intent.putExtra("secondIndex", otherUserIdx)
            startActivity(intent)
        }

        val userMyPageAdapter = UserMyPageVPAdapter(this)
        binding.userMypageVp.adapter = userMyPageAdapter

        TabLayoutMediator(binding.userMypageTb, binding.userMypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
            }
        }.attach()

        binding.userMypageFollowTv.setOnClickListener {  // 팔로우 리스트에서 언팔 및 팔로우 시 visibility 변경
            binding.userMypageFollowTv.visibility = View.INVISIBLE
            binding.userMypageUnfollowTv.visibility = View.VISIBLE
            binding.userMypageFollowerCntTv.text = "${followerCnt++}"
            followerCnt += 1

            val userFollowService = UserFollowService() // 팔로우
            userFollowService.setUserFollowView(this)
            userFollowService.userFollow(getJwt()!!, otherUserIdx!!)
        }

        binding.userMypageUnfollowTv.setOnClickListener {
            binding.userMypageFollowTv.visibility = View.VISIBLE
            binding.userMypageUnfollowTv.visibility = View.INVISIBLE
            binding.userMypageFollowerCntTv.text = (followerCnt - 1).toString()
            followerCnt -= 1
            val userUnfollowService = UserUnfollowService() // 언팔로우
            userUnfollowService.setUserUnfollowView(this)
            userUnfollowService.userUnfollow(getJwt()!!, otherUserIdx!!)
        }

        moveFollowActivity()
    }

    private fun moveFollowActivity() {
        binding.userMypageFollowing.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("nickName", nickName)
            intent.putExtra("current", 0)
            intent.putExtra("userIdx", otherUserIdx)
            startActivity(intent)
        }

        binding.userMypageFollower.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("nickName", nickName)
            intent.putExtra("current", 1)
            intent.putExtra("userIdx", otherUserIdx)
            startActivity(intent)
        }
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
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

    private fun showPopup() {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, binding.userMypageMoreIv, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.user_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.user_report) {
                // 유저 신고하기
                val reportDialog = ReportDialog(getJwt()!!, 0, secondIndex, secondIndex)
                reportDialog.isCancelable = false
                reportDialog.show(supportFragmentManager, "report")
            }
            else {  // 댓글 신고하기
                Log.d("BLOCK", "USER")
            }

            false
        }

        popupMenu.show() // 팝업 보여주기
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetOtherUserResult) {
        synchronized(this) {
            Log.d("MYPAGE", result.toString())

            Glide.with(this).load(result.getUser.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                .into(binding.userMypageProfileimgIv)
            profileImg = result.getUser.profileImgUrl

            nickName = result.getUser.nickName
            secondIndex = result.getUser.userIdx

            binding.userMypageNicknameTv.text = nickName
            binding.userMypageInfoNicknameTv.text = nickName // 닉네임 연동

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
            binding.userMypageFollowingCntTv.text = result.getUser.followerCount.toString()
            binding.userMypageFollowerCntTv.text = result.getUser.followeeCount.toString()
            followerCnt = result.getUser.followeeCount
            binding.userMypagePortfolioCntTv.text = result.getUser.pofolCount.toString()

            setSession(result.getUser.userSession)  // 세션 연동

            if (result.getUser.follow == 0){
                binding.userMypageFollowTv.visibility = View.VISIBLE
                binding.userMypageUnfollowTv.visibility = View.INVISIBLE
            } else {
                binding.userMypageFollowTv.visibility = View.INVISIBLE
                binding.userMypageUnfollowTv.visibility = View.VISIBLE
            }
        }
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