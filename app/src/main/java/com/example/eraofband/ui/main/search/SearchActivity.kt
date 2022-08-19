package com.example.eraofband.ui.main.search

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
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
import com.example.eraofband.ui.main.search.band.SearchBandInterface
import com.example.eraofband.ui.main.search.lesson.SearchLessonFragment
import com.example.eraofband.ui.main.search.lesson.SearchLessonInterface
import com.example.eraofband.ui.main.search.user.SearchUserFragment
import com.example.eraofband.ui.main.search.user.SearchUserInterface
import com.google.android.material.tabs.TabLayoutMediator


class SearchActivity : AppCompatActivity(), GetSearchUserView, GetSearchBandView, GetSearchLessonView {
    private lateinit var binding: ActivitySearchBinding
    private var bandInit = false // 밴드 프래그먼트 초기화 여부
    private var lessonInit = false // 레슨 프래그먼트 초기화 여부

    private var S: String? = null // 검색어

    private lateinit var searchUserInterface: SearchUserInterface
    private lateinit var searchUserFragment: SearchUserFragment

    private lateinit var searchBandInterface: SearchBandInterface
    private lateinit var searchBandFragment: SearchBandFragment

    private lateinit var searchLessonInterface: SearchLessonInterface
    private lateinit var searchLessonFragment: SearchLessonFragment

    private val userService = GetSearchUserService()  // 검색 관련 api
    private val bandService = GetSearchBandService()
    private val lessonService = GetSearchLessonService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    S = s.toString()

                    userService.getSearchUser(s.toString()) //  s = 검색 문자열
                    if (bandInit)  // 프래그먼트에 한번이라도 접근을 했는가?
                        bandService.getSearchBand(s.toString())
                    if (lessonInit)
                       lessonService.getSearchLesson(s.toString())
                }
                if (s.toString() == ""){ // 문자열이 비어있을 때 리스트 초기화
                    searchUserInterface.initUserRV(arrayListOf())
                    if (bandInit)
                        searchBandInterface.initBandRV(arrayListOf())
                    if (lessonInit)
                        searchLessonInterface.initLessonRV(arrayListOf())
                }
            }
        })
    }

    fun setUserView(userFragment: SearchUserFragment) {
        searchUserFragment = userFragment
        searchUserInterface = searchUserFragment
    }

    fun setBandView(bandFragment: SearchBandFragment) {
        searchBandFragment = bandFragment
        searchBandInterface = searchBandFragment
        bandInit = true // 프래그먼트 접근 true
        if (S != null)
          bandService.getSearchBand(S!!)
    }

    fun setLessonView(lessonFragment: SearchLessonFragment) {
        searchLessonFragment = lessonFragment
        searchLessonInterface = searchLessonFragment
        bandInit = true
        if (S != null)
            lessonService.getSearchLesson(S!!)
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
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onGetSearchUserSuccess(result: List<GetSearchUserResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
        searchUserInterface.initUserRV(result)
    }

    override fun onGetSearchUserFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }

    override fun onGetSearchBandSuccess(result: List<GetSearchBandResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
        searchBandInterface.initBandRV(result)
    }

    override fun onGetSearchBandFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }

    override fun onGetSearchLessonSuccess(result: List<GetSearchLessonResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
        searchLessonFragment.initLessonRV(result)
    }

    override fun onGetSearchLessonFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }
}