package com.example.eraofband.ui.main.home.lessonlike

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentHomeLessonLikeBinding
import com.example.eraofband.ui.main.home.lesson.LessonInfoActivity
import com.example.eraofband.remote.getLessonLikeList.GetLessonLikeListService
import com.example.eraofband.remote.lesson.getLikeLessonList.GetLessonLikeListResult
import com.example.eraofband.remote.lesson.getLikeLessonList.GetLessonLikeListView


class HomeLessonLikeFragment : Fragment(), GetLessonLikeListView {
    private var _binding: FragmentHomeLessonLikeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeLessonLikeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val lessonLikeList = GetLessonLikeListService()
        lessonLikeList.getLessonLikeListView(this)
        lessonLikeList.getLessonLikeList(getJwt()!!)
    }

    private fun initRecyclerView(list: List<GetLessonLikeListResult>) {
        // 레슨 리사이클러뷰
        val lessonLikeListRVAdapter = HomeLessonLikeRVAdapter(list)
        binding.homeLessonLikeRv.adapter = lessonLikeListRVAdapter
        binding.homeLessonLikeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        lessonLikeListRVAdapter.setMyItemClickListener(object : HomeLessonLikeRVAdapter.MyItemClickListener {
            override fun onShowDetail(lessonIdx: Int) {  // 레슨 모집 페이지로 전환
                var intent = Intent(context, LessonInfoActivity::class.java)
                intent.putExtra("lessonIdx", lessonIdx)
                startActivity(intent)
            }
        })
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetLessonLikeListSuccess(code: Int, result: List<GetLessonLikeListResult>) {
        Log.d("GET/SUCCESS", "$result")
        initRecyclerView(result)
    }

    override fun onGetLessonLikeListFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }
}