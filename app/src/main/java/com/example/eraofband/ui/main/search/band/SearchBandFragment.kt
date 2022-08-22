package com.example.eraofband.ui.main.search.band

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchBandBinding
import com.example.eraofband.remote.search.getBand.GetSearchBandResult
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.search.SearchActivity
import com.example.eraofband.ui.main.search.user.SearchUserRVAdapter
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class SearchBandFragment : Fragment(), SearchBandInterface {

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

    override fun onResume() {
        super.onResume()
        (activity as SearchActivity).setBandView(this)
    }

    private fun initRVAdapter(bandList: List<GetSearchBandResult>){
        val searchBandRVAdapter = SearchBandRVAdapter()
        binding.searchBandRv.adapter = searchBandRVAdapter
        binding.searchBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchBandRVAdapter.setMyItemClickListener(object : SearchBandRVAdapter.MyItemClickListener{
            override fun bandInfo(bandIdx: Int) {
                val intent = Intent(context, BandRecruitActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)
                startActivity(intent)
            }
        })
        searchBandRVAdapter.initBandList(bandList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initBandRV(result: List<GetSearchBandResult>) {
        initRVAdapter(result)
    }
}