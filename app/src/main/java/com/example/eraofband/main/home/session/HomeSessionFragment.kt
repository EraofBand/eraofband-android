package com.example.eraofband.main.home.session

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentHomeSessionBinding
import com.example.eraofband.main.home.session.band.BandListActivity
import com.example.eraofband.remote.getNewBand.GetNewBandResult
import com.example.eraofband.remote.getNewBand.GetNewBandService
import com.example.eraofband.remote.getNewBand.GetNewBandView

class HomeSessionFragment : Fragment(), GetNewBandView {


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

        // 생성된 밴드 리스트 확인 용
        binding.homeCl1.setOnClickListener{ startActivity(Intent(context, BandListActivity::class.java)) }

    }

    override fun onResume() {
        super.onResume()

        val getService =  GetNewBandService()
        getService.setBandView(this)
        getService.getNewBand()
    }

    private fun initRecyclerView(item: List<GetNewBandResult>) {
        val homeNewBandRVAdapter = HomeSessionNewBandRVAdapter(context!!)

        binding.homeNewBandRv.adapter = homeNewBandRVAdapter
        binding.homeNewBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeNewBandRVAdapter.initNewBand(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetSuccess(result: List<GetNewBandResult>) {

        Log.d("GETNEWBAND/SUC", result.toString())
        initRecyclerView(result)  // 새로 생성된 밴드 리스트를 리사이클러뷰에 넣어줌
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETNEWBAND/FAIL", "$code $message")
    }
}