package com.example.eraofband.ui.main.mypage.portfolio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eraofband.databinding.FragmentMypagePortfolioBinding
import com.example.eraofband.remote.user.getMyPage.*

class MyPagePortfolioFragment : Fragment(), GetMyPageView {

    private var _binding: FragmentMypagePortfolioBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMypagePortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val getMyPagePofol = GetMyPageService()
        getMyPagePofol.setUserView(this)
        getMyPagePofol.getMyInfo(getJwt()!!, getUserIdx())
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun connectAdapter(item : List<GetUserPofol>) {
        val mAdapter = MyPagePortfolioRVAdapter(context!!)
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
}
