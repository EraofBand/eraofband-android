package com.example.eraofband.main.home.bandlike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentHomeBandLikeBinding


class HomeBandLikeFragment : Fragment() {
    private var _binding: FragmentHomeBandLikeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBandLikeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        var likeBandList = arrayListOf(
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", "")
        )

        val homeBandLikeRVAdapter = HomeBandLikeRVAdapter(likeBandList)

        binding.homeBandLikeRv.adapter = homeBandLikeRVAdapter
        binding.homeBandLikeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}