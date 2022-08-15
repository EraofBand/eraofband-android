package com.example.eraofband.ui.main.mypage.follow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.eraofband.databinding.FragmentFollowingBinding
import com.example.eraofband.remote.user.userFollowList.FollowingInfo
import com.example.eraofband.remote.user.userFollowList.UserFollowListResult
import com.example.eraofband.remote.user.userFollowList.UserFollowListService
import com.example.eraofband.remote.user.userFollowList.UserFollowListView
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import java.util.*

class FollowingFragment(var userIdx: Int) : Fragment(), UserFollowListView {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var mAdapter: FollowingRVAdapter
    private lateinit var followings : List<FollowingInfo>
    private var searchLists = ArrayList<FollowingInfo>()
    private val userFollowList = UserFollowListService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            if(binding.followingSearchEt.isFocused) hideKeyboard()
        }

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

        userFollowList.setUserFollowListView(this)
        userFollowList.userFollowList(getJwt()!!, userIdx)

    }

    private fun connectAdapter(item: List<FollowingInfo>) {
        mAdapter = FollowingRVAdapter()
        binding.followingRv.adapter = mAdapter
        binding.followingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initFollowList(item)

        val animator: RecyclerView.ItemAnimator? = binding.followingRv.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        mAdapter.setMyItemClickListener(object : FollowingRVAdapter.MyItemClickListener {
            override fun onItemClick(item: FollowingInfo) {
                val intent = Intent(activity, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", item.userIdx)
                startActivity(intent)
            }

            override fun clickMySelf() {
                startActivity(Intent(activity, MyPageActivity::class.java))
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
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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