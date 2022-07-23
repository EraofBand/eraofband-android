package com.example.eraofband.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.main.mypage.MyPageBandFragment
import com.example.eraofband.main.mypage.MyPageLessonFragment
import com.example.eraofband.main.mypage.portfolio.MyPagePortfolioFragment

class HomeVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeSessionFragment()
            1 -> HomeLessonFragment()
            2 -> HomeBandLikeFragment()
            else -> HomeLessonLikeFragment()
        }
    }
}