package com.example.eraofband.ui.main.home.session.band.album

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentBandRecruitAlbumBinding
import com.example.eraofband.remote.band.getAlbumAlbumBand.GetAlbumBandService
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandResult
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandView


class BandRecruitAlbumFragment : Fragment(), GetAlbumBandView {
    private lateinit var binding: FragmentBandRecruitAlbumBinding
    private lateinit var albumRVAdapter: BandAlbumRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBandRecruitAlbumBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences("band", AppCompatActivity.MODE_PRIVATE)
        val bandIdx = sp.getInt("bandIdx",0)

        val albumService = GetAlbumBandService()
        albumService.setAlbumBandView(this)
        albumService.getAlbumBand(getJwt()!!, bandIdx)

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initRV(item: List<GetAlbumBandResult>) {
        albumRVAdapter = BandAlbumRVAdapter()
        binding.bandRecruitAlbumRv.adapter = albumRVAdapter
        binding.bandRecruitAlbumRv.layoutManager = LinearLayoutManager(context, VERTICAL, false)

        // 리사이클러뷰 구분선 추가
        val decoration = DividerItemDecoration(context, VERTICAL)
        decoration.setDrawable(context?.resources!!.getDrawable(R.drawable.divider_rv))
        binding.bandRecruitAlbumRv.addItemDecoration(decoration)

        albumRVAdapter.initAlbum(item)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: List<GetAlbumBandResult>) {
        Log.d("GET ALBUM / SUCCESS", result.toString())
        initRV(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET ALBUM / FAILURE", "$code $message")
    }
}