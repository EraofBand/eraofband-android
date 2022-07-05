package com.example.eraofband.onboarding


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingVPAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    var fragments = listOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position)
    }
}