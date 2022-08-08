package com.example.eraofband.ui.main.search.band

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchBandBinding
import com.example.eraofband.remote.search.getBand.GetSearchBandResult

class SearchBandFragment : Fragment() {

    private var _binding: FragmentSearchBandBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBandBinding.inflate(inflater, container, false)

        return binding.root
    }
    fun initRVAdapter(bandList: List<GetSearchBandResult>){
        val searchBandRVAdapter = SearchBandRVAdapter()
        binding.searchBandRv.adapter = searchBandRVAdapter
        binding.searchBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchBandRVAdapter.initBandList(bandList)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}