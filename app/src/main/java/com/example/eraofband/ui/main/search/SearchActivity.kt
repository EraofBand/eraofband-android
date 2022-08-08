package com.example.eraofband.ui.main.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.eraofband.databinding.ActivitySearchBinding
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.remote.search.getUser.GetSearchUserService
import com.example.eraofband.remote.search.getUser.GetSearchUserView
import com.example.eraofband.remote.user.userFollowList.FollowerInfo
import com.example.eraofband.ui.main.mypage.follow.FollowerRVAdapter
import com.example.eraofband.ui.main.search.user.SearchUserRVAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(), GetSearchUserView {
    private lateinit var binding: ActivitySearchBinding

    private val mAdapter = SearchUserRVAdapter()
    private lateinit var searchUsers : List<GetSearchUserResult>
    private var searchLists = ArrayList<GetSearchUserResult>()

    private var getUserService = GetSearchUserService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBackIb.setOnClickListener {
            finish()
        }

        val current = intent.getIntExtra("current", 0)

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


        mAdapter.clear()

        getUserService.setBandView(this)

        binding.searchBarEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchId = binding.searchBarEt.text.toString()
                getUserService.getSearchUser(searchId)
                binding.searchBarEt.hint = ""
            }
        })
    }

    override fun onResume() {
        super.onResume()

    }


    override fun onGetSearchUserSuccess(result: List<GetSearchUserResult>) {
        Log.d("SEARCH USER / SUCCESS", result.toString())
        searchUsers = result
    }

    override fun onGetSearchUserFailure(code: Int, message: String) {
        Log.d("SEARCH USER / FAIL", "$code $message")
    }
}