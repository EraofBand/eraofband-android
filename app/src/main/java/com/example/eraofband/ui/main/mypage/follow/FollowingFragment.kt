package com.example.eraofband.ui.main.mypage.follow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentFollowingBinding
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import com.example.eraofband.remote.user.userFollowList.*
import com.example.eraofband.ui.main.mypage.MyPageActivity
import java.util.*
import kotlin.collections.ArrayList

class FollowingFragment(var userIdx: Int) : Fragment(), UserFollowListView {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private val mAdapter = FollowingRVAdapter()
    private lateinit var followings : List<FollowingInfo>
    private var searchLists = ArrayList<FollowingInfo>()
    private val userFollowList = UserFollowListService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        binding.followingSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchId = binding.followingSearchEt.text.toString()
                searchFilter(searchId)
                binding.followingSearchEt.hint = ""
            }
        })
        return binding.root
    }

    private fun searchFilter(id: String) {
        searchLists.clear()
        for (i in followings.indices) {
            if (followings[i].nickName.toLowerCase()
                    .contains(id.lowercase(Locale.getDefault()))
            ) {
                searchLists.add(followings[i])
            }
        }
        mAdapter.filterList(searchLists)
    }

    override fun onResume() {
        super.onResume()// GET 해당 유저 팔로우리스트
        if (binding.followingSearchEt.hint == "") {
            binding.followingSearchEt.text = null
            binding.followingSearchEt.hint = "팔로잉 검색창"
        }
        mAdapter.clear()
        userFollowList.setUserFollowListView(this)
        userFollowList.userFollowList(getJwt()!!, userIdx)
    }

    private fun connectAdapter(item: List<FollowingInfo>) {
        binding.followingRv.adapter = mAdapter
        binding.followingRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.setMyItemClickListener(object : FollowingRVAdapter.MyItemClickListener {
            override fun onItemClick(item: FollowingInfo) {
                val intent = Intent(context, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", item.userIdx)
                startActivity(intent)
            }

            override fun clickMySelf() {
                startActivity(Intent(context, MyPageActivity::class.java))
            }

            override fun getJwt(): String? {
                val userSP =
                    requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
                return userSP.getString("jwt", "")
            }

            override fun getUserIdx(): Int {
                val userSP =
                    requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
                return userSP.getInt("userIdx", 0)
            }
        })
        mAdapter.initFollowList(item)
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onUserFollowListSuccess(code: Int, result: UserFollowListResult) {
        Log.d("FOLLOWLIST", "$code $result")
        connectAdapter(result.getfollowing)
        followings = result.getfollowing
    }

    override fun onUserFollowListFailure(code: Int, message: String) {
        Log.d("FOLLOWLIST", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}