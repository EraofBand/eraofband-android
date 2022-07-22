package com.example.eraofband.main.home.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentHomeLessonBinding

class HomeLessonFragment: Fragment() {

    private var _binding: FragmentHomeLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeLessonBinding.inflate(inflater, container, false)

        initRecyclerView()
        initSpinner()

        return binding.root
    }

    private fun initSpinner() {  // 스피너 초기화
        // 지역 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.capital)  // 전체, 서울, 경기도

        val cityAdapter = ArrayAdapter(context!!, R.layout.item_spinner, city)
        binding.homeLessonCitySp.adapter = cityAdapter
        binding.homeLessonCitySp.setSelection(0)

        // 지역 스피너 클릭 이벤트
        binding.homeLessonCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
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
                binding.homeLessonCitySp.setSelection(0)
                initRecyclerView()
            }

        })
    }

    private fun initRecyclerView() {
        // 레슨 리사이클러뷰
        val lessonRVAdapter = LessonRVAdapter()
        binding.homeLessonListRv.adapter = lessonRVAdapter
        binding.homeLessonListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", "")
        )

        lessonRVAdapter.initLessonList(bandList)
    }
}