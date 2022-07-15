package com.example.eraofband.main.mypage

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
import com.example.eraofband.databinding.FragmentMypageBinding
import com.example.eraofband.remote.getuser.GetUserResult
import com.example.eraofband.remote.getMyPage.GetMyPageService
import com.example.eraofband.remote.getuser.GetUserView
import com.example.eraofband.main.MainActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.eraofband.main.mypage.follow.FollowFragment
import com.example.eraofband.main.mypage.portfolio.PortfolioMakeActivity
import com.example.eraofband.remote.getMyPage.GetMyPageView
import com.example.eraofband.remote.getuser.GetMyPageResult
import com.example.eraofband.remote.getuser.GetUserService
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class MyPageFragment : Fragment(), GetMyPageView {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mypageProfileEditIv.setOnClickListener {
            startActivity(Intent(activity, ProfileEditActivity::class.java))
        }

        binding.mypageSettingIv.setOnClickListener {
            startActivity(Intent(activity, MyPageSettingActivity::class.java))
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


        binding.mypageFollowingCntTv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FollowFragment(0)).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.mypageFollowerCntTv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FollowFragment(1)).addToBackStack(null).commitAllowingStateLoss()
        }

        binding.mypageFab.setOnClickListener{
            startActivity(Intent(activity, PortfolioMakeActivity::class.java))
        }
        connectVP()
    }

    override fun onStart() {
        super.onStart()

        // 나중에 프로필 편집 하게 되면 값이 바껴야하니까 onStart에 넣어줬어요
        // 유저 정보를 받아온 후 프로필 편집 화면에 연동
        val getMyPageService = GetMyPageService()

        getMyPageService.setUserView(this)
        getMyPageService.getUser(getJwt()!!, getUserIdx())
    }

//----------------------------------------------------------------------------------------------------

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        // 나중에 프사도 연동 예정, 포트폴리오는 아직

        Log.d("MYPAGE", result.toString())
        binding.mypageNicknameTv.text = result.getUser.nickName  // 닉네임 연동

        // 디테일한 소개 연동
        val index = result.getUser.region.indexOf(" ")
        val city = result.getUser.region.substring(0, index)
        val index = result.region.split(" ")
        val city = index[1]

        val age = setDate().substring(0, 4).toInt() - result.getUser.birth.substring(0, 4).toInt() + 1

        val gender =
            if(result.getUser.gender == "MALE") "남성"
            else "여성"

        binding.mypageDetailInfoTv.text = "$city | ${age}세 | $gender"

        binding.mypageIntroductionTv.text = result.getUser.instroduction  // 내 소개 연동
//        binding.mypageIntroductionTv.text = "ddddddddddddddddddddddddddddddddddddddffffddddddddddddddddddddddddddddddddddddddddddd"  // 3줄 테스트용

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
        binding.mypageFollowingCntTv.text = result.getUser.followeeCount.toString()
        binding.mypageFollowerCntTv.text = result.getUser.followerCount.toString()
        binding.mypagePortfolioCntTv.text = result.getUser.pofolCount.toString()

        setSession(result.getUser.session)  // 세션 연동
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
            3 -> binding.mypageSessionTv.text = "드럼"
            else ->  binding.mypageSessionTv.text = "키보드"
        }
    }
}


