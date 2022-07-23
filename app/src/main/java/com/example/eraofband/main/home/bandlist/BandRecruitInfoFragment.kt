package com.example.eraofband.main.home.bandlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentBandRecruitInfoBinding

class BandRecruitInfoFragment: Fragment() {

    private var _binding: FragmentBandRecruitInfoBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var memberRVAdapter: BandMemberRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBandRecruitInfoBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root

    }

    private fun initRecyclerView() {
        memberRVAdapter = BandMemberRVAdapter()
        binding.bandRecruitInfoMemberRv.adapter = memberRVAdapter
        binding.bandRecruitInfoMemberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", "")
        )

        memberRVAdapter.initMemberList(bandList)

    }
}