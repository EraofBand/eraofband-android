package com.example.eraofband.ui.main.search.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentSearchLessonBinding

class SearchLessonFragment : Fragment() {

    private var _binding: FragmentSearchLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchLessonBinding.inflate(inflater, container, false)

        return binding.root
    }
}