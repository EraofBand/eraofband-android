package com.example.eraofband.ui.main.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.ui.main.search.band.SearchBandFragment
import com.example.eraofband.ui.main.search.lesson.SearchLessonFragment
import com.example.eraofband.ui.main.search.user.SearchUserFragment

class SearchVPAdapter (fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {  // 프레그먼트 포지션에 따른 프레그먼트 띄우기
            0 -> SearchUserFragment()
            1 -> SearchBandFragment()
            else -> SearchLessonFragment()
        }
    }
}