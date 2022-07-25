package com.example.eraofband.main.home.session

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentHomeSessionBinding
import com.example.eraofband.main.home.session.band.BandListActivity

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

        // 생성된 밴드 리스트 확인 용
        binding.homeCl1.setOnClickListener{ startActivity(Intent(context, BandListActivity::class.java)) }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        var newBandList = arrayListOf(
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", ""),
            Band(0, "", "")
        )

        val homeNewBandRVAdapter = HomeSessionNewBandRVAdapter(newBandList)

        binding.homeNewBandRv.adapter = homeNewBandRVAdapter
        binding.homeNewBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}