package com.example.eraofband.ui.main.home.lesson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentHomeLessonBinding
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListResult
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListService
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListView

class HomeLessonFragment: Fragment(), GetLessonListView {

    private var _binding: FragmentHomeLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    val getLessonList = GetLessonListService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeLessonBinding.inflate(inflater, container, false)

        getLessonList.getLessonListView(this)
        initSpinner()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getLessonList.getLessonList("전체", 5)
    }

    private fun initSpinner() {  // 스피너 초기화
        // 지역 스피너 어뎁터 연결
        val city = resources.getStringArray(R.array.capital)  // 전체, 서울, 경기도

        val cityAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, city)
        binding.homeLessonCitySp.adapter = cityAdapter
        binding.homeLessonCitySp.setSelection(0)

        // 지역 스피너 클릭 이벤트
        binding.homeLessonCitySp.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        regionSessionSelect("전체")
                    }
                    1 -> {
                        regionSessionSelect("서울")
                    }
                   else -> {
                       regionSessionSelect("경기도")
                   }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 전체를 띄워줌
                binding.homeLessonCitySp.setSelection(0)
                getLessonList.getLessonList("전체", 5) // 모든 지역이 뜨도록
            }

        })
    }

    private fun regionSessionSelect(region: String) {
        binding.homeLessonSessionCg.isSelectionRequired = true

        binding.homeLessonTotalCp.isChecked = true
        getLessonList.getLessonList(region, 5) // 자동 전체 세션 초기화
        binding.homeLessonTotalCp.setOnClickListener { getLessonList.getLessonList(region, 5)}
        binding.homeLessonVocalCp.setOnClickListener { getLessonList.getLessonList(region, 0)}
        binding.homeLessonGuitarCp.setOnClickListener { getLessonList.getLessonList(region, 1)}
        binding.homeLessonBaseCp.setOnClickListener { getLessonList.getLessonList(region, 2)}
        binding.homeLessonKeyboardCp.setOnClickListener { getLessonList.getLessonList(region, 3)}
        binding.homeLessonDrumCp.setOnClickListener { getLessonList.getLessonList(region, 4)}
    }

    private fun initRecyclerView(lessonList: List<GetLessonListResult>) {
        // 레슨 리사이클러뷰
        val lessonListRVAdapter = LessonListRVAdapter()
        binding.homeLessonListRv.adapter = lessonListRVAdapter
        binding.homeLessonListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        lessonListRVAdapter.initLessonList(lessonList)

        lessonListRVAdapter.setMyItemClickListener(object : LessonListRVAdapter.MyItemClickListener {
            override fun onShowDetail(lessonIdx: Int) {  // 레슨 모집 페이지로 전환
                var intent = Intent(context, LessonInfoActivity::class.java)
                intent.putExtra("lessonIdx", lessonIdx)
                startActivity(intent)
            }
        })
    }

    override fun onGetLessonListSuccess(code: Int, result: List<GetLessonListResult>) {
        Log.d("MAKE/SUCCESS", result.toString())
        initRecyclerView(result)
    }

    override fun onGetLessonListFailure(code: Int, message: String) {
        Log.d("MAKE/SUCCESS", "${code}, $message")
    }
}