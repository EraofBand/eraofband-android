package com.example.eraofband.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.ui.main.home.session.HomeSessionFragment
import com.example.eraofband.ui.main.home.bandlike.HomeBandLikeFragment
import com.example.eraofband.ui.main.home.lesson.HomeLessonFragment
import com.example.eraofband.ui.main.home.lessonlike.HomeLessonLikeFragment

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