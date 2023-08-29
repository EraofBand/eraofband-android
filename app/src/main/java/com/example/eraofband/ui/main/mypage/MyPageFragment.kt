package com.example.eraofband.ui.main.mypage

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Report
import com.example.eraofband.databinding.FragmentMypageBinding
import com.example.eraofband.remote.user.getMyPage.GetMyPageResult
import com.example.eraofband.remote.user.getMyPage.GetMyPageService
import com.example.eraofband.remote.user.getMyPage.GetMyPageView
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.ui.main.mypage.follow.FollowActivity
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioMakeActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class MyPageFragment : Fragment(), GetMyPageView, RefreshJwtView {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private var mySession : Int = -1
    private lateinit var nickName : String

    private val getMyPageService = GetMyPageService()
    private val refreshJwtService = RefreshJwtService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getMyPageService.setUserView(this)
        refreshJwtService.setRefreshView(this)

        if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
            refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, getUserIdx())
        } else {
            getMyPageService.getMyInfo(getJwt()!!, getUserIdx())
        }

        clickListener()
    }
//----------------------------------------------------------------------------------------------------
    private fun clickListener() {
        binding.mypageProfileEditIv.setOnClickListener {
            startActivity(Intent(activity, ProfileEditActivity::class.java))
        }

        binding.mypageSettingIv.setOnClickListener {
            startActivity(Intent(activity, MyPageSettingActivity::class.java))
        }
        binding.mypageSessionChangeTv.setOnClickListener {
            val intent = Intent(activity, MyPageSessionActivity::class.java)
            intent.putExtra("session", mySession)
            startActivity(intent)
        }

        binding.mypageVp.registerOnPageChangeCallback( object :  // 뷰페이저 리스너 : 포트폴리오 페이지에서만 FAB를 표시해줌
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> binding.mypageFab.visibility = View.VISIBLE
                    1 -> binding.mypageFab.visibility = View.INVISIBLE
                    2 -> binding.mypageFab.visibility = View.INVISIBLE
                }
            }
        })

        binding.mypageFab.setOnClickListener{
            startActivity(Intent(activity, PortfolioMakeActivity::class.java))
        }

        connectVP()
        moveFollowActivity()
    }

    private fun moveFollowActivity() {
        binding.mypageFollowing.setOnClickListener {
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra("current", 0)
            intent.putExtra("nickName", nickName)
            intent.putExtra("userIdx", getUserIdx())
            startActivity(intent)
        }

        binding.mypageFollower.setOnClickListener {
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra("current", 1)
            intent.putExtra("nickName", nickName)
            intent.putExtra("userIdx", getUserIdx())
            startActivity(intent)
        }
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    }

    private fun connectVP() {
        val myPageAdapter = MyPageVPAdapter(this)
        binding.mypageVp.adapter = myPageAdapter

        TabLayoutMediator(binding.mypageTb, binding.mypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
                2 -> tab.text = "신청 레슨"
            }
        }.attach()
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        synchronized(this) {
            Log.d("MYPAGE", result.toString())

            // 닉네임 연동
            nickName = result.getUser.nickName
            binding.mypageNicknameTv.text = nickName

            // 글라이드를 이용한 프로필사진 연동
            Glide.with(this).load(result.getUser.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.mypageProfileimgIv)

            // 프사 url 저장
            val profileSP = requireActivity().getSharedPreferences("profile", MODE_PRIVATE)
            val editor = profileSP.edit()
            editor.putString("url", result.getUser.profileImgUrl)
            editor.apply()

            // 디테일한 소개 연동
            val index = result.getUser.region.split(" ")
            val city = index[1]

            val age = setDate().substring(0, 4).toInt() - result.getUser.birth.substring(0, 4).toInt() + 1

            val gender =
                if(result.getUser.gender == "MALE") "남성"
                else "여성"

            binding.mypageDetailInfoTv.text = "$city | ${age}세 | $gender"
            binding.mypageIntroductionTv.text = result.getUser.introduction  // 내 소개 연동

            if(binding.mypageIntroductionTv.lineCount > 3) {
                binding.mypageLookMoreTv.visibility = View.VISIBLE  // 더보기 표시

                // 더보기 클릭 이벤트
                binding.mypageLookMoreTv.setOnClickListener {
                    if (binding.mypageLookMoreTv.text == "더보기") {
                        binding.mypageLookMoreTv.text = "접기"
                        binding.mypageIntroductionTv.maxLines = 100
                    }
                    else {
                        binding.mypageLookMoreTv.text = "더보기"
                        binding.mypageIntroductionTv.maxLines = 3
                    }
                }
            }

            // 숫자 연동
            binding.mypageFollowingCntTv.text = result.getUser.followerCount.toString()
            binding.mypageFollowerCntTv.text = result.getUser.followeeCount.toString()
            binding.mypagePortfolioCntTv.text = result.getUser.pofolCount.toString()

            setSession(result.getUser.userSession)  // 세션 연동
            mySession = result.getUser.userSession
            when(result.getUser.userSession) {
                0 -> binding.mypageSessionIv.setImageResource(R.drawable.ic_mypage_session_vocal)
                1 -> binding.mypageSessionIv.setImageResource(R.drawable.ic_mypage_session_guitar)
                2 -> binding.mypageSessionIv.setImageResource(R.drawable.ic_mypage_session_base)
                3 -> binding.mypageSessionIv.setImageResource(R.drawable.ic_mypage_session_keyboard)
                4 -> binding.mypageSessionIv.setImageResource(R.drawable.ic_mypage_session_drum)
            }
        }
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPAGE/FAIL", "$code $message")
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() : String {  // 오늘 날짜 불러오기
        val today = System.currentTimeMillis()  // 현재 날짜, 시각 불러오기
        val date = Date(today)
        val mFormat = SimpleDateFormat("yyyy-MM-dd")

        return mFormat.format(date)
    }

    private fun setSession(session : Int) {
        // 나중에 이미지 확정되면 이미지도 넣을 예정
        when (session) {
            0 -> binding.mypageSessionTv.text = "보컬"
            1 -> binding.mypageSessionTv.text = "기타"
            2 -> binding.mypageSessionTv.text = "베이스"
            3 -> binding.mypageSessionTv.text = "키보드"
            else ->  binding.mypageSessionTv.text = "드럼"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")
        getMyPageService.getMyInfo(getJwt()!!, getUserIdx())
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REPORT/FAIL", "$code $message")
    }
}


