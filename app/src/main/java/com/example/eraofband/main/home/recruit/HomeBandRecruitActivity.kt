package com.example.eraofband.main.home.recruit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ActivityHomeBandRecruitBinding

class HomeBandRecruitActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBandRecruitBinding
    private lateinit var recruitRVAdapter: RecruitRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBandRecruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandBackIv.setOnClickListener { finish() }

        initSpinner()

    }

    private fun initSpinner() {  // 스피너 초기화
        // 지역 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.capital)  // 전체, 서울, 경기도

        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, city)
        binding.homeBandCitySp.adapter = cityAdapter
        binding.homeBandCitySp.setSelection(0)

        // 지역 스피너 클릭 이벤트
        binding.homeBandCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
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
                binding.homeBandCitySp.setSelection(0)
                initRecyclerView()
            }

        })
    }

    private fun initRecyclerView() {
        recruitRVAdapter = RecruitRVAdapter()
        binding.homeBandListRv.adapter = recruitRVAdapter
        binding.homeBandListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""))

        recruitRVAdapter.initBandList(bandList)
    }
}