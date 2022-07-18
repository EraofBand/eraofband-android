package com.example.eraofband.main.usermypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentUserMypageBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserMyPageFragment : Fragment() {

    private var _binding: FragmentUserMypageBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserMypageBinding.inflate(inflater, container, false)

        val userMyPageAdapter = UserMyPageVPAdapter(this)
        binding.userMypageVp.adapter = userMyPageAdapter

        TabLayoutMediator(binding.userMypageTb, binding.userMypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
            }
        }.attach()

        return binding.root
    }


}