package com.example.eraofband.ui.main.search.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchLessonBinding
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.ui.main.search.user.SearchUserRVAdapter

class SearchLessonFragment : Fragment() {

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

    fun initRVAdapter(lessonList: List<GetSearchLessonResult>){
        val searchLessonRVAdapter = SearchLessonRVAdapter()
        binding.searchLessonRv.adapter = searchLessonRVAdapter
        binding.searchLessonRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchLessonRVAdapter.initLessonList(lessonList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}