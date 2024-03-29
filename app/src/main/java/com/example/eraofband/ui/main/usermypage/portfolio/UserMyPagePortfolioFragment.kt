package com.example.eraofband.ui.main.usermypage.portfolio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eraofband.databinding.FragmentUserMypagePortfolioBinding
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolView
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

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

    override fun onResume() {
        super.onResume()

        val getMypofol = GetMyPofolService()
        getMypofol.setPofolView(this)
        getMypofol.getPortfolio(getJwt()!!, getUserIdx())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserIdx(): Int {  //임시방편입니다요...
        val userIdx = (activity as UserMyPageActivity).otherUserIdx
        return userIdx!!.toInt()
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun connectAdapter(item : List<GetPofolResult>) {
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

    override fun onGetSuccess(result: List<GetPofolResult>) {
        Log.d("PORTFOLIO/SUC", result.toString())
        connectAdapter(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("PORTFOLIO/FAIL", "$code $message")
    }

}