package com.example.eraofband.main.home.lesson

import android.content.Intent
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

class HomeLessonFragment: Fragment(), GetLessonListView {

    private var _binding: FragmentHomeLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private val getLessonList = GetLessonListService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeLessonBinding.inflate(inflater, container, false)

        initSpinner()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getLessonList.getLessonListView(this)
        getLessonList.getLessonList("전체", 5)
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
                when(position) {
//                    0 -> getLessonList.getLessonList("전체", 5) // 모든 지역이 뜨도록
//                    1 -> getLessonList.getLessonList("서울", 5) // 서울 지역만 뜨도록
//                    else -> getLessonList.getLessonList("경기도", 5) // 경기도 지역만 뜨도록
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  // 아무것도 클릭되어있지 않을 때는 기본으로 전체를 띄워줌
                binding.homeLessonCitySp.setSelection(0)
//                getLessonList.getLessonList("전체", 5) // 모든 지역이 뜨도록
            }

        })
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