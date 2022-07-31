package com.example.eraofband.ui.main.home.session

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentHomeSessionBinding
import com.example.eraofband.ui.main.home.session.band.BandListActivity
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity
import com.example.eraofband.ui.main.home.session.band.HomeSessionPopularBandRVAdapter
import com.example.eraofband.remote.band.getNewBand.GetNewBandResult
import com.example.eraofband.remote.band.getNewBand.GetNewBandService
import com.example.eraofband.remote.band.getNewBand.GetNewBandView
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandResult
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandService
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandView

class HomeSessionFragment : Fragment(), GetNewBandView, GetPopularBandView {


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

        // 새로 생성된 밴드 리스트
        val getNewService =  GetNewBandService()
        getNewService.setBandView(this)
        getNewService.getNewBand()

        // TOP3 밴드 리스트
        val getPopularService = GetPopularBandService()
        getPopularService.setBandView(this)
        getPopularService.getPopularBand()
    }

    private fun initNewBandRV(item: List<GetNewBandResult>) {
        val homeNewBandRVAdapter = HomeSessionNewBandRVAdapter(context!!)

        binding.homeNewBandRv.adapter = homeNewBandRVAdapter

        binding.homeNewBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeNewBandRVAdapter.initNewBand(item)
        homeNewBandRVAdapter.setMyItemClickListener(object : HomeSessionNewBandRVAdapter.MyItemClickListener{
            override fun onShowBandInfo(bandIdx: Int) {
                // bandIdx를 넘겨주면서 밴드 모집 페이지 전환
                val intent = Intent(activity, BandRecruitActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)
                startActivity(intent)
            }

        })
    }

    private fun initPopularBandRV(item: List<GetPopularBandResult>) {
        val popularBandRVAdapter = HomeSessionPopularBandRVAdapter(context!!)

        binding.homePopularBandRv.adapter = popularBandRVAdapter
        binding.homePopularBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        popularBandRVAdapter.initPopularBand(item)

        popularBandRVAdapter.setMyItemClickListener(object : HomeSessionPopularBandRVAdapter.MyItemClickListener{
            override fun onShowBandInfo(bandIdx: Int) {
                // bandIdx를 넘겨주면서 밴드 모집 페이지 전환
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

    override fun onGetNewSuccess(result: List<GetNewBandResult>) {
        Log.d("GETNEWBAND/SUC", result.toString())

        // 새로 생성된 밴드 리스트 정보 연결
        initNewBandRV(result)
    }

    override fun onGetNewFailure(code: Int, message: String) {
        Log.d("GETNEWBAND/FAIL", "$code $message")
    }

    override fun onGetPopSuccess(result: List<GetPopularBandResult>) {
        Log.d("GETPOPBAND/SUC", result.toString())
        
        // TOP3 밴드 정보 연결
        initPopularBandRV(result)
    }

    override fun onGetPopFailure(code: Int, message: String) {
        Log.d("GETPOPBAND/FAIL", "$code $message")
    }
}