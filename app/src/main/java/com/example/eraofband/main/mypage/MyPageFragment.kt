package com.example.eraofband.main.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentMypageBinding
import com.example.eraofband.login.LoginActivity
import com.example.eraofband.main.MainActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient

class MyPageFragment : Fragment() {

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
        binding.mypageIntroductionTv.post{  // 내소개 더보기 리스너
            val lineCount = binding.mypageIntroductionTv.layout.lineCount
            if (lineCount > 0) {
                if (binding.mypageIntroductionTv.layout.getEllipsisCount(lineCount - 1) > 0) {
                    // 더보기 표시
                    binding.mypageLookMoreTv.visibility = View.VISIBLE

                    // 더보기 클릭 이벤트
                    binding.mypageIntroductionTv.setOnClickListener {
                    }
                }
            }
        }
        connectVP()
        logout()
        resign()
    }

//----------------------------------------------------------------------------------------------------

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

    private fun logout() {
        binding.logout.setOnClickListener {  // 로그아웃 프로세스
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(context, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                activity?.finish()  // 로그아웃시 스택에 있는 메인 액티비티 종료
            }
        }
    }

    private fun resign() {
        binding.resign.setOnClickListener {  // 회원탈퇴 프로세스
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(context, "회원탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                    activity?.finish()  // 로그아웃시 스택에 있는 메인 액티비티 종료
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


