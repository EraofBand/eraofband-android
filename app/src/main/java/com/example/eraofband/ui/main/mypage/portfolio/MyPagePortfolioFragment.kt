package com.example.eraofband.ui.main.mypage.portfolio

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eraofband.data.Report
import com.example.eraofband.databinding.FragmentMypagePortfolioBinding
import com.example.eraofband.remote.user.getMyPage.*
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult

class MyPagePortfolioFragment : Fragment(), GetMyPageView, RefreshJwtView {

    private var _binding: FragmentMypagePortfolioBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private val getMyPagePofol = GetMyPageService()
    private val refreshJwtService = RefreshJwtService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMypagePortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        getMyPagePofol.setUserView(this)
        refreshJwtService.setRefreshView(this)

        if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
            refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, getUserIdx())
        } else {
            getMyPagePofol.getMyInfo(getJwt()!!, getUserIdx())
        }
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    }

    private fun connectAdapter(item : List<GetUserPofol>) {
        val mAdapter = MyPagePortfolioRVAdapter(requireContext())
        binding.mypagePortfolioRv.adapter = mAdapter

        val gridLayoutManager = GridLayoutManager(context,3)
        binding.mypagePortfolioRv.layoutManager = gridLayoutManager

        mAdapter.initPortfolio(item)

        mAdapter.setMyItemClickListener(object : MyPagePortfolioRVAdapter.MyItemClickListener {
            override fun onLookDetail(position : Int) {  // 프로필 리스트 화면 전환
                val intent = Intent(context, PortfolioListActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        Log.d("PORTFOLIO/SUC", result.toString())
        connectAdapter(result.getUserPofol)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("PORTFOLIO/FAIL", "$code $message")
    }

    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")
        getMyPagePofol.getMyInfo(getJwt()!!, getUserIdx())
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REFRESH/FAIL", "$code $message")
    }
}
