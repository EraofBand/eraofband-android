package com.example.eraofband.ui.main.mypage.follow

import android.content.Intent
import android.os.Bundle
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

    override fun onResume() {
        super.onResume()
        val userFollowList = UserFollowListService() // GET 해당 유저 팔로우리스트
        userFollowList.setUserFollowListView(this)
        userFollowList.userFollowList(getJwt()!!, userIdx)
    }

    private fun connectAdapter(item: List<FollowingInfo>) {
        val mAdapter = FollowingRVAdapter()
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
    }

    override fun onUserFollowListFailure(code: Int, message: String) {
        Log.d("FOLLOWLIST", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}