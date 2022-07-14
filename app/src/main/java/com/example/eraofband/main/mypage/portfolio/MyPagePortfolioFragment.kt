package com.example.eraofband.main.mypage.portfolio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.FragmentMypagePortfolioBinding

class MyPagePortfolioFragment : Fragment() {
    private var _binding: FragmentMypagePortfolioBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private var portfolioItem = arrayListOf(
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, ""),
        Portfolio("", R.drawable.portfolio_spare, "", 0, "")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMypagePortfolioBinding.inflate(inflater, container, false)

        connectAdapter()

        return binding.root
    }

    private fun connectAdapter() {
        val mAdapter = MyPagePortfolioRVAdapter(portfolioItem)
        binding.mypagePortfolioRv.adapter = mAdapter

        val gridLayoutManager = GridLayoutManager(context,3)
        binding.mypagePortfolioRv.layoutManager = gridLayoutManager

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
}
