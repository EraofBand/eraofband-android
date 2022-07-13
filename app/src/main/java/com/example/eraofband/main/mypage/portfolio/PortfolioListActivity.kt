package com.example.eraofband.main.mypage.portfolio

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityPortfolioListBinding

class PortfolioListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPortfolioListBinding
    private var pofolList = arrayListOf(
        Portfolio("나의 소중한 포트폴리오", R.drawable.ic_captain, "제목입니다", 0, ""),
        Portfolio("나의 소중한 포트폴리오", R.drawable.ic_captain, "제목입니다", 0, ""),
        Portfolio("나의 소중한 포트폴리오", R.drawable.ic_captain, "제목입니다", 0, ""),
        Portfolio("나의 소중한 포트폴리오", R.drawable.ic_captain, "제목입니다", 0, ""),
        Portfolio("나의 소중한 포트폴리오", R.drawable.ic_captain, "제목입니다", 0, "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioListBackIv.setOnClickListener { finish() }  // 뒤로 가기

    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val portfolioAdapter = PortfolioListRVAdapter(pofolList)
        binding.portfolioListPortfolioRv.adapter = portfolioAdapter
        binding.portfolioListPortfolioRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        portfolioAdapter.setMyItemClickListener(object : PortfolioListRVAdapter.MyItemListener {
            override fun urlParse(url: String): Uri {
                return Uri.parse("android.resource://$packageName/raw/video3")
//                return Uri.parse(url)  원래는 이걸 사용해야함
            }
        })
    }
}