package com.example.eraofband.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.main.home.band.HomeBandFragment
import com.example.eraofband.main.home.bandlike.HomeBandLikeFragment
import com.example.eraofband.main.home.lesson.HomeLessonFragment
import com.example.eraofband.main.home.lessonlike.HomeLessonLikeFragment

class HomeVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeBandFragment()
            1 -> HomeLessonFragment()
            2 -> HomeBandLikeFragment()
            else -> HomeLessonLikeFragment()
        }
    }
}