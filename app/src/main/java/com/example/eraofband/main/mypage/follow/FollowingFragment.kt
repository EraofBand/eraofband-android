package com.example.eraofband.main.mypage.follow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentFollowingBinding
import com.example.eraofband.main.usermypage.UserMyPageActivity
import com.example.eraofband.remote.userfollowlist.*

class FollowingFragment(var userIdx: Int) : Fragment(), UserFollowListView {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userFollowList = UserFollowListService()
        userFollowList.setUserFollowListView(this)
        userFollowList.userFollowList(userIdx)
    }

    private fun connectAdapter(item: List<FollowingInfo>) {
        val mAdapter = FollowingRVAdapter()
        binding.followingRv.adapter = mAdapter
        binding.followingRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.setMyItemClickListener(object : FollowingRVAdapter.MyItemClickListener {
            override fun onItemClick(item: FollowingInfo) {
                val intent = Intent(context, UserMyPageActivity::class.java)
                intent.putExtra("comment", item.userIdx)
                startActivity(intent)
            }
        })
        mAdapter.initFollowList(item)
    }

    override fun onUserFollowListSuccess(code: Int, result: UserFollowListResult) {
        Log.d("FOLLOWLIST", "$code $result")
        connectAdapter(result.getfollowing)
    }

    override fun onUserFollowListFailure(code: Int, message: String) {
        Log.d("FOLLOWLIST", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}