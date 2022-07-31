package com.example.eraofband.ui.main.home.bandlike

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentHomeBandLikeBinding
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandResult
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandService
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandView
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity


class HomeBandLikeFragment : Fragment(), GetLikedBandView {
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
    }

    override fun onResume() {
        super.onResume()

        val getBandService = GetLikedBandService()
        getBandService.setBandView(this)
        getBandService.getLikedBand(getJwt()!!)
    }

    private fun initRecyclerView(item: List<GetLikedBandResult>) {
        val bandLikeRVAdapter = HomeBandLikeRVAdapter(context!!)

        binding.homeBandLikeRv.adapter = bandLikeRVAdapter
        binding.homeBandLikeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bandLikeRVAdapter.initLikedBand(item)

        bandLikeRVAdapter.setMyItemClickListener(object : HomeBandLikeRVAdapter.MyItemClickListener{
            override fun onShowBandInfo(bandIdx: Int) {
                val intent = Intent(activity, BandRecruitActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)
                startActivity(intent)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: List<GetLikedBandResult>) {
        Log.d("GETLIKEDBAND/SUC", "$result")
        initRecyclerView(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETLIKEDBAND/FAIL", "$code $message")
    }
}