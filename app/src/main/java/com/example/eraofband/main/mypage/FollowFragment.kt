package com.example.eraofband.main.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentFollowBinding
import com.example.eraofband.databinding.FragmentFollowerBinding
import com.example.eraofband.onboarding.*
import com.google.android.material.tabs.TabLayoutMediator

class FollowFragment(private val current: Int) : Fragment() {
    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뷰페이저 어뎁터 연결
        val fragmentList = listOf(FollowingFragment(), FollowerFragment())
        val followVPAdapter = FollowVPAdapter(this)
        followVPAdapter.fragments = fragmentList
        binding.followVp.adapter = followVPAdapter

        //탭레이아웃 연결
        TabLayoutMediator(binding.followTl, binding.followVp) { tab, position ->
            when (position) {
                0 -> tab.text = "팔로잉"
                1 -> tab.text = "팔로워"
            }
        }.attach()

        if(current == 0){
            binding.followVp.currentItem = 0
        }else{
            binding.followVp.currentItem = 1
        }
    }

}