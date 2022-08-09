package com.example.eraofband.ui.main.search.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchUserBinding
import com.example.eraofband.remote.lesson.getLessonInfo.LessonMembers
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.ui.main.home.lesson.LessonStudentRVAdapter
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class SearchUserFragment: Fragment() {

    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initRVAdapter()
        Log.d("userF","ok")
    }

    private fun initRVAdapter(){
        val searchUserRVAdapter = SearchUserRVAdapter()
        binding.searchUserRv.adapter = searchUserRVAdapter
        binding.searchUserRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}