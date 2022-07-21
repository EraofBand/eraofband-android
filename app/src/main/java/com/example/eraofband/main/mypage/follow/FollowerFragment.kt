package com.example.eraofband.main.mypage.follow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentFollowerBinding
import com.example.eraofband.databinding.FragmentFollowingBinding
import com.example.eraofband.databinding.FragmentMypagePortfolioBinding
import com.example.eraofband.main.mypage.portfolio.MyPagePortfolioRVAdapter
import com.example.eraofband.main.mypage.portfolio.PortfolioCommentRVAdapter
import com.example.eraofband.main.mypage.portfolio.PortfolioListActivity
import com.example.eraofband.main.usermypage.UserMyPageActivity
import com.example.eraofband.remote.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.getMyPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.PofolCommentResult
import com.example.eraofband.remote.userfollowlist.FollowerInfo
import com.example.eraofband.remote.userfollowlist.UserFollowListResult
import com.example.eraofband.remote.userfollowlist.UserFollowListService
import com.example.eraofband.remote.userfollowlist.UserFollowListView

class FollowerFragment(var userIdx: Int) : Fragment(), UserFollowListView {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userFollowList = UserFollowListService() // GET 해당 유저 팔로우리스트
        userFollowList.setUserFollowListView(this)
        userFollowList.userFollowList(userIdx)


    }

    private fun connectAdapter(item : List<FollowerInfo>) {
        val mAdapter = FollowerRVAdapter()
        binding.followingRv.adapter = mAdapter // 리사이클러뷰 어댑터 연결
        binding.followingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.setMyItemClickListener(object : FollowerRVAdapter.MyItemClickListener {

            override fun onItemClick(item: FollowerInfo) {
                val intent = Intent(context, UserMyPageActivity::class.java)
                intent.putExtra("comment", item.userIdx)
                startActivity(intent)
            }
        })
        mAdapter.initFollowList(item) // 팔로우리스트 초기화
    }

    override fun onUserFollowListSuccess(code: Int, result: UserFollowListResult) {
        Log.d("FOLLOWLIST", "$code $result")
        connectAdapter(result.getfollower) // 서버에서 받은 팔로우 리스트 전달
    }

    override fun onUserFollowListFailure(code: Int, message: String) {
        Log.d("FOLLOWLIST", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}