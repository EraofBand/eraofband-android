package com.example.eraofband.ui.main.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivitySearchBinding
import com.example.eraofband.remote.search.getBand.GetSearchBandResult
import com.example.eraofband.remote.search.getBand.GetSearchBandService
import com.example.eraofband.remote.search.getBand.GetSearchBandView
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult
import com.example.eraofband.remote.search.getLesson.GetSearchLessonService
import com.example.eraofband.remote.search.getLesson.GetSearchLessonView
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.remote.search.getUser.GetSearchUserService
import com.example.eraofband.remote.search.getUser.GetSearchUserView
import com.example.eraofband.ui.main.search.band.SearchBandFragment
import com.example.eraofband.ui.main.search.lesson.SearchLessonFragment
import com.example.eraofband.ui.main.search.user.SearchUserFragment
import com.example.eraofband.ui.main.search.user.SearchUserRVAdapter
import com.google.android.material.tabs.TabLayoutMediator


class SearchActivity : AppCompatActivity(), GetSearchUserView, GetSearchBandView, GetSearchLessonView {
    private lateinit var binding: ActivitySearchBinding
    private var nowPage = 0

    private var userfragmet = SearchUserFragment()

    internal var userLists = arrayListOf<GetSearchUserResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userService = GetSearchUserService()  // 검색 관련 api
        val bandService = GetSearchBandService()
        val lessonService = GetSearchLessonService()

        userService.setUserView(this)
        bandService.setBandView(this)
        lessonService.setLessonView(this)

        binding.searchBackIb.setOnClickListener {
            finish()
        }

        initVPAdapter() // 뷰페이저 초기화

        binding.searchBarEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    when (nowPage) {
                        0 -> {userService.getSearchUser(s.toString()) }  //  s = 검색 문자열
                        1 -> bandService.getSearchBand(s.toString())
                        else -> lessonService.getSearchLesson(s.toString())
                    }
                }
            }
        })
    }

    private fun initVPAdapter() {
        val current = intent.getIntExtra("current", 0) // 메인에서 들어왔는지, 밴드에서 들어왔는지

        val searchVPAdapter = SearchVPAdapter(this)
        binding.searchVp.adapter = searchVPAdapter

        TabLayoutMediator(binding.searchTb, binding.searchVp) { tab, position ->
            when (position) {
                0 -> tab.text = "유저"
                1 -> tab.text = "밴드"
                else -> tab.text = "레슨"
            }
        }.attach()

        if(current == 1){
            binding.searchVp.post {
                binding.searchVp.currentItem = 1
            }
        }

        // 뷰페이저 리스너 (현재 뷰페이저가 어디를 띄어주고 있는지)
        binding.searchVp.registerOnPageChangeCallback((object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                nowPage = when(position) {
                    0 -> 0    // userFragment
                    1 -> 1    // bandFragment
                    else -> 2 // lessonFragment
                }
            }
        }))
    }

    override fun onGetSearchUserSuccess(result: List<GetSearchUserResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
        userLists = result as ArrayList<GetSearchUserResult>

        val bundle = Bundle()
        bundle.putParcelableArrayList("list", userLists)
        userfragmet.arguments = bundle


        SearchUserRVAdapter().clear()
        SearchUserRVAdapter().initUserList(result)
    }

    override fun onGetSearchUserFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }

    override fun onGetSearchBandSuccess(result: List<GetSearchBandResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
    }

    override fun onGetSearchBandFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }

    override fun onGetSearchLessonSuccess(result: List<GetSearchLessonResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
    }

    override fun onGetSearchLessonFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }
}