package com.example.eraofband.ui.main.board

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.databinding.FragmentBoardBinding
import com.example.eraofband.remote.portfolio.getPofol.GetOtherPofolService
import com.example.eraofband.remote.portfolio.getPofol.GetOtherPofolView
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioCommentActivity
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioMakeActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class BoardFragment : Fragment(), GetOtherPofolView {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var feedRVAdapter: BoardFeedRVAdapter
    private val pofolService = GetOtherPofolService()

    private val total = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.boardTopEditIv.setOnClickListener { startActivity(Intent(activity, PortfolioMakeActivity::class.java)) }

        binding.boardTotalTv.setOnClickListener {
            if(!total) {
                binding.boardTotalTv.setBackgroundColor(Color.parseColor("#1864FD"))  // 파란색

                pofolService.getTotalPortfolio(getJwt()!!, 0)
                binding.boardFeedRv.smoothScrollToPosition(0)  // 상단으로 이동
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        pofolService.setPofolView(this)
        pofolService.getTotalPortfolio(getJwt()!!, 0)
    }

    private fun initFeedRV(item: List<GetPofolResult>) {
        feedRVAdapter = BoardFeedRVAdapter(getJwt()!!, requireContext())
        binding.boardFeedRv.adapter = feedRVAdapter
        binding.boardFeedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        feedRVAdapter.initFeed(item)

        // 스크롤 상단, 하단 감지
        binding.boardFeedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding.boardFeedRv.canScrollVertically(1)) {  // 맨 위
                    Log.d("SCROLL", "TOP")
                }
                else if (binding.boardFeedRv.canScrollVertically(-1)) {  // 맨 아래
                    Log.d("SCROLL", "BOTTOM")
                    Log.d("GET / SUCCESS", "${feedRVAdapter.itemCount}")

                    if(feedRVAdapter.itemCount % 20 == 0) {
                        // 마지막 인덱스를 어케 알죠 좀 더 고민해봐야할 듯
//                        pofolService.getTotalPortfolio(getJwt()!!, 80)
                    }
                }
                else {
                    Log.d("SCROLL", "IDLE")
                }
            }
        })

        feedRVAdapter.setMyItemClickListener(object : BoardFeedRVAdapter.MyItemListener{
            override fun urlParse(url: String): Uri {
                return Uri.parse(url)
            }

            override fun onShowComment(pofolIdx: Int) {  // 댓글 창 띄우기
                val intent = Intent(activity, PortfolioCommentActivity::class.java)
                intent.putExtra("pofolIdx", pofolIdx)
                startActivity(intent)
            }

            override fun onShowPopup(portfolio: GetPofolResult, position: Int, view: View) {
//                showPopup(portfolio, position, view)
            }

            override fun onShowInfoPage(userIdx: Int) {
                if(userIdx == getUserIdx()) {
                    val intent = Intent(activity, MyPageActivity::class.java)
                    intent.putExtra("userIdx", userIdx)
                    startActivity(intent)
                }
                else {
                    val intent = Intent(activity, UserMyPageActivity::class.java)
                    intent.putExtra("userIdx", userIdx)
                    startActivity(intent)
                }

            }
        })
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetTotalSuccess(result: List<GetPofolResult>) {
        Log.d("GET/SUC", "$result")
        initFeedRV(result)
    }

    override fun onGetTotalFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

    override fun onGetFollowSuccess(result: List<GetPofolResult>) {
        TODO("Not yet implemented")
    }

    override fun onGetFollowFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }
}