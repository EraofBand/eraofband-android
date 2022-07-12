package com.example.eraofband.main.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {

    var fragments = listOf<Fragment>()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> FollowingFragment()
            else -> FollowerFragment()
        }
    }
}