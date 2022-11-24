package com.example.eraofband.ui.main.search.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchUserBinding
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.ui.main.community.CommunityFeedRVAdapter
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.search.SearchActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class SearchUserFragment: Fragment(), SearchUserInterface {

    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private var searchUser = arrayListOf<GetSearchUserResult>()


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
        (activity as SearchActivity).setUserView(this)
    }

    private fun initRVAdapter(result: List<GetSearchUserResult>){
        val searchUserRVAdapter = SearchUserRVAdapter()
        binding.searchUserRv.adapter = searchUserRVAdapter
        binding.searchUserRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchUserRVAdapter.setMyItemClickListener(object : SearchUserRVAdapter.MyItemClickListener{
            override fun userInfo(userIdx: Int) {
                if(userIdx == getUserIdx()) {
                    startActivity(Intent(context, MyPageActivity::class.java))
                }  // 만약 누른 유저가 본인일 경우
                else {
                    val intent = Intent(context, UserMyPageActivity::class.java)
                    intent.putExtra("userIdx", userIdx)
                    startActivity(intent)
                }  // 다른 유저일 경우
            }
        })
        searchUserRVAdapter.initUserList(result)
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initUserRV(result: List<GetSearchUserResult>) {
        initRVAdapter(result)
    }
}