package com.example.eraofband.ui.main.home.session.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityBandListBinding
import com.example.eraofband.remote.band.getRegionBand.GetRegionBandResult
import com.example.eraofband.remote.band.getRegionBand.GetRegionBandService
import com.example.eraofband.remote.band.getRegionBand.GetRegionBandView

class BandListActivity: AppCompatActivity(), GetRegionBandView {

    private lateinit var binding: ActivityBandListBinding
    private lateinit var bandListRVAdapter: BandListRVAdapter
    private var initial = true
    private var sessionValue = 0

    val getRegionBandService = GetRegionBandService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()

        sessionValue = intent.getIntExtra("sessionBtn", 0)


        binding.homeBandListBackIv.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()
        getRegionBandService.setGetView(this)
        getRegionBandService.getRegionBand("전체", 5)
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
                if(initial) {
                    binding.homeBandListCitySp.setSelection(0)
                    sessionSelect(sessionValue)
                    initial = false
                }
                else {
                    when (position) {
                        0 -> regionSessionSelect("전체")
                        1 -> regionSessionSelect("서울")
                        2 -> regionSessionSelect("경기도")
                    }
                }
                }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 전체를 띄워줌
                binding.homeBandListCitySp.setSelection(0)
                getRegionBandService.getRegionBand("전체", 5)
            }

        })
    }

    private fun regionSessionSelect(region : String) {
        binding.homeBandListTotalCp.isChecked = true
        getRegionBandService.getRegionBand(region, 5) // 자동 전체 세션 초기화
        binding.homeBandListTotalCp.setOnClickListener { getRegionBandService.getRegionBand(region, 5)}
        binding.homeBandListVocalCp.setOnClickListener { getRegionBandService.getRegionBand(region, 0)}
        binding.homeBandListGuitarCp.setOnClickListener { getRegionBandService.getRegionBand(region, 1)}
        binding.homeBandListBaseCp.setOnClickListener { getRegionBandService.getRegionBand(region, 2)}
        binding.homeBandListKeyboardCp.setOnClickListener { getRegionBandService.getRegionBand(region, 3)}
        binding.homeBandListDrumCp.setOnClickListener { getRegionBandService.getRegionBand(region, 4)}
    }

    private fun sessionSelect(sessionValue : Int) {
        if(sessionValue == 0){
            binding.homeBandListVocalCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        } else if (sessionValue == 1) {
            binding.homeBandListGuitarCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        } else if (sessionValue == 2){
            binding.homeBandListBaseCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        } else if (sessionValue == 3){
            binding.homeBandListKeyboardCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        } else if (sessionValue == 4){
            binding.homeBandListDrumCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        } else{
            binding.homeBandListTotalCp.isChecked = true
            getRegionBandService.getRegionBand("전체", sessionValue)
        }
    }

    private fun initRecyclerView(regionBand : List<GetRegionBandResult>) {
        bandListRVAdapter = BandListRVAdapter()
        binding.homeBandListListRv.adapter = bandListRVAdapter
        binding.homeBandListListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        bandListRVAdapter.cleanBandList(regionBand)
        bandListRVAdapter.initBandList(regionBand)

        bandListRVAdapter.setMyItemClickListener(object : BandListRVAdapter.MyItemClickListener{
            override fun onShowDetail(bandIdx: Int) {  // 밴드 모집 페이지로 전환
                val intent = Intent(this@BandListActivity, BandRecruitActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)
                startActivity(intent)
            }
        })
    }

    override fun onGetSuccess(code: Int, result: List<GetRegionBandResult>) {
        Log.d("GET BAND / SUCCESS", result.toString())
        initRecyclerView(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET BAND / FAIL", "$code $message")
    }
}