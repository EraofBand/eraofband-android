package com.example.eraofband.ui.main.home.session.band.album

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentBandRecruitAlbumBinding


class BandRecruitAlbumFragment : Fragment() {

    private lateinit var binding: FragmentBandRecruitAlbumBinding

    private lateinit var albumRVAdapter: BandAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBandRecruitAlbumBinding.inflate(inflater, container, false)

        val string = listOf("음", "냠", "냠")

        initRV(string)

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initRV(item: List<String>) {
        albumRVAdapter = BandAlbumRVAdapter(requireContext())

        binding.bandRecruitAlbumRv.adapter = albumRVAdapter
        binding.bandRecruitAlbumRv.layoutManager = LinearLayoutManager(context, VERTICAL, false)

        // 리사이클러뷰 구분선 추가
        val decoration = DividerItemDecoration(context, VERTICAL)
        decoration.setDrawable(context?.resources!!.getDrawable(R.drawable.divider_rv))
        binding.bandRecruitAlbumRv.addItemDecoration(decoration)

        albumRVAdapter.initAlbum(item)

    }
}