package com.example.eraofband.ui.main.search.lesson

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchLessonBinding
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult
import com.example.eraofband.ui.main.home.lesson.LessonInfoActivity
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity
import com.example.eraofband.ui.main.search.SearchActivity
import com.example.eraofband.ui.main.search.band.SearchBandRVAdapter

class SearchLessonFragment : Fragment(), SearchLessonInterface {

    private var _binding: FragmentSearchLessonBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchLessonBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as SearchActivity).setLessonView(this)
    }

    private fun initRVAdapter(lessonList: List<GetSearchLessonResult>){
        val searchLessonRVAdapter = SearchLessonRVAdapter()
        binding.searchLessonRv.adapter = searchLessonRVAdapter
        binding.searchLessonRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchLessonRVAdapter.setMyItemClickListener(object : SearchLessonRVAdapter.MyItemClickListener{
            override fun lessonInfo(lessonIdx: Int) {
                val intent = Intent(context, LessonInfoActivity::class.java)
                intent.putExtra("lessonIdx", lessonIdx)
                startActivity(intent)
            }
        })
        searchLessonRVAdapter.initLessonList(lessonList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initLessonRV(result: List<GetSearchLessonResult>) {
        initRVAdapter(result)
    }
}