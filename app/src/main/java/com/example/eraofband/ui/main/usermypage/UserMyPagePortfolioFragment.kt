package com.example.eraofband.ui.main.usermypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eraofband.databinding.FragmentUserMypagePortfolioBinding
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolView

class UserMyPagePortfolioFragment : Fragment(), GetMyPofolView {

    private var _binding: FragmentUserMypagePortfolioBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserMypagePortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val getMypofol = GetMyPofolService()
        getMypofol.setPofolView(this)
        getMypofol.getPortfolio(getUserIdx())
    }

    private fun getUserIdx(): Int {  //임시방편입니다요...
        val userIdx = (activity as UserMyPageActivity).otherUserIdx
        return userIdx!!.toInt()
    }

    private fun connectAdapter(item : List<GetMyPofolResult>) {
        val mAdapter = UserMyPagePortfolioRVAdapter()
        binding.userMypagePortfolioRv.adapter = mAdapter

        val gridLayoutManager = GridLayoutManager(context,3)
        binding.userMypagePortfolioRv.layoutManager = gridLayoutManager

        mAdapter.initPortfolio(item)

        mAdapter.setMyItemClickListener(object : UserMyPagePortfolioRVAdapter.MyItemClickListener {
            override fun onLookDetail(position : Int) {  // 프로필 리스트 화면 전환
                val intent = Intent(context, UserPortfolioListActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("userIdx", getUserIdx())
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetSuccess(result: List<GetMyPofolResult>) {
        Log.d("PORTFOLIO/SUC", result.toString())
        connectAdapter(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("PORTFOLIO/FAIL", "$code $message")
    }

}