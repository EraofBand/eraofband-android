package com.example.eraofband.ui.main.home.lessonlike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.eraofband.databinding.FragmentHomeLessonLikeBinding

class HomeLessonLikeFragment : Fragment() {
    private var _binding: FragmentHomeLessonLikeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeLessonLikeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
       /* var likeLessonList = arrayListOf(
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", "")
        )

        val homeLessonLikeRVAdapter = HomeLessonLikeRVAdapter(likeLessonList)

        binding.homeLessonLikeRv.adapter = homeLessonLikeRVAdapter
        binding.homeLessonLikeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}