package com.example.eraofband.ui.main.usermypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.ui.main.usermypage.band.UserMyPageBandFragment
import com.example.eraofband.ui.main.usermypage.portfolio.UserMyPagePortfolioFragment

class UserMyPageVPAdapter (fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> UserMyPagePortfolioFragment()
            else -> UserMyPageBandFragment()
        }
    }
}