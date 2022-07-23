package com.example.eraofband.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentHomeBinding
import com.example.eraofband.databinding.FragmentHomeLessonLikeBinding
import com.example.eraofband.databinding.FragmentHomeSessionBinding

class HomeSessionFragment : Fragment() {


    private var _binding: FragmentHomeSessionBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeSessionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}