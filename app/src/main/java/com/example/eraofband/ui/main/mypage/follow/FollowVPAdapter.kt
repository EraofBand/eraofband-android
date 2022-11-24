package com.example.eraofband.ui.main.mypage.follow

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowVPAdapter(fragmentActivity: FragmentActivity, var userIdx: Int) : FragmentStateAdapter(fragmentActivity) {

    var fragments = listOf<Fragment>()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> FollowingFragment(userIdx)
            else -> FollowerFragment(userIdx)
        }
    }
}