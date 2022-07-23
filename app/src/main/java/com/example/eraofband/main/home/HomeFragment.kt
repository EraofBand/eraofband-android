package com.example.eraofband.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectVP()
    }

    private fun connectVP() {
        val homeAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeAdapter

        TabLayoutMediator(binding.homeTb, binding.homeVp) { tab, position ->
            when (position) {
                0 -> tab.text = "세션 매칭"
                1 -> tab.text = "레슨 매칭"
                2 -> tab.text = "찜한 밴드"
                3 -> tab.text = "찜한 레슨"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}