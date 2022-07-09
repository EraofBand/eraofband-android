package com.example.eraofband.main.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> MyPagePortfolioFragment()
            1 -> MyPageBandFragment()
            else -> MyPageLessonFragment()
        }
    }
}