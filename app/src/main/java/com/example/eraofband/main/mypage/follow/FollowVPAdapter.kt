package com.example.eraofband.main.mypage.follow

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.main.mypage.follow.FollowerFragment
import com.example.eraofband.main.mypage.follow.FollowingFragment

class FollowVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    var fragments = listOf<Fragment>()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> FollowingFragment()
            else -> FollowerFragment()
        }
    }
}