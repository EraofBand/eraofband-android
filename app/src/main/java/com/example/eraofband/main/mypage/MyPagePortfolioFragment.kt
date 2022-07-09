package com.example.eraofband.main.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentMypageBinding
import com.example.eraofband.databinding.FragmentMypagePortfolioBinding

class MyPagePortfolioFragment : Fragment() {
    private var _binding: FragmentMypagePortfolioBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypagePortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
