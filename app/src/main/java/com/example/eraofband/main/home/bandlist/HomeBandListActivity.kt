package com.example.eraofband.main.home.bandlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ActivityHomeBandListBinding

class HomeBandListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBandListBinding
    private lateinit var recruitRVAdapter: BandListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBandListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandListBackIv.setOnClickListener { finish() }

        initSpinner()

    }

    private fun initSpinner() {  // 스피너 초기화
        // 지역 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.capital)  // 전체, 서울, 경기도

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.homeBandListCitySp.adapter = cityAdapter
        binding.homeBandListCitySp.setSelection(0)

        // 지역 스피너 클릭 이벤트
        binding.homeBandListCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {  // 모든 지역이 뜨도록
                    initRecyclerView()
                }
                else if(position == 1){  // 서울 지역만 뜨도록

                }
                else {  // 경기도 지역만 뜨도록

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 전체를 띄워줌
                binding.homeBandListCitySp.setSelection(0)
                initRecyclerView()
            }

        })
    }

    private fun initRecyclerView() {
        recruitRVAdapter = BandListRVAdapter()
        binding.homeBandListListRv.adapter = recruitRVAdapter
        binding.homeBandListListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""))

        recruitRVAdapter.initBandList(bandList)

        recruitRVAdapter.setMyItemClickListener(object : BandListRVAdapter.MyItemClickListener{
            override fun onShowDetail(bandIdx: Int) {  // 밴드 모집 페이지로 전환
                startActivity(Intent(this@HomeBandListActivity, HomeBandRecruitActivity::class.java))
            }
        })
    }
}