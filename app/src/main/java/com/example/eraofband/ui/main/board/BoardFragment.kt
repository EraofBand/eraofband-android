package com.example.eraofband.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentBoardBinding
import com.example.eraofband.ui.main.home.HomeVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class BoardFragment : Fragment(){

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectVP()
    }

    private fun connectVP() {
        val boardAdapter = BoardVPAdapter(this)
        binding.boardVp.adapter = boardAdapter

        TabLayoutMediator(binding.boardTb, binding.boardVp) { tab, position ->
            when (position) {
                0 -> tab.text = "자유"
                1 -> tab.text = "질문"
                2 -> tab.text = "홍보"
                3 -> tab.text = "거래"
                else -> tab.text = "MY"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}