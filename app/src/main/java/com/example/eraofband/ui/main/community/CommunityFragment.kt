package com.example.eraofband.ui.main.community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentCommunityBinding
import com.example.eraofband.remote.portfolio.getPofol.GetOtherPofolService
import com.example.eraofband.remote.portfolio.getPofol.GetOtherPofolView
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.ui.main.home.session.band.BandDeleteDialog
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.mypage.portfolio.PofolEditActivity
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioCommentActivity
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioMakeActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class CommunityFragment : Fragment(), GetOtherPofolView {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var feedRVAdapter: CommunityFeedRVAdapter
    private val pofolService = GetOtherPofolService()

    private var total = true
    private var lastPofolIdx = 0
    private var add = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.communityTopEditIv.setOnClickListener { startActivity(Intent(activity, PortfolioMakeActivity::class.java)) }

        binding.communityTotalTv.setOnClickListener {
            if(!total) {
                binding.communityTotalTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색
                binding.communityFollowTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색

                add = false
                pofolService.getTotalPortfolio(getJwt()!!, 0)
                binding.communityFeedRv.smoothScrollToPosition(0)  // 상단으로 이동

                total = true
            }
        }

        binding.communityFollowTv.setOnClickListener {
            if(total) {
                binding.communityTotalTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색
                binding.communityFollowTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색

                add = false
                pofolService.getFollowPortfolio(getJwt()!!, 0)
                binding.communityFeedRv.smoothScrollToPosition(0)  // 상단으로 이동

                total = false
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        add = false
        pofolService.setPofolView(this)
        pofolService.getTotalPortfolio(getJwt()!!, 0)
    }

    private fun initFeedRV(item: List<GetPofolResult>) {
        feedRVAdapter = CommunityFeedRVAdapter(getJwt()!!, requireContext())
        binding.communityFeedRv.adapter = feedRVAdapter
        binding.communityFeedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        feedRVAdapter.initFeed(item)

        // 스크롤 상단, 하단 감지
        binding.communityFeedRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding.communityFeedRv.canScrollVertically(1)) {  // 맨 위
                    Log.d("SCROLL", "TOP")
                }
                else if (binding.communityFeedRv.canScrollVertically(-1)) {  // 맨 아래
                    Log.d("SCROLL", "BOTTOM")
                    Log.d("SCROLL / SUCCESS", "${feedRVAdapter.itemCount}")

                    if(feedRVAdapter.itemCount % 10 == 0) {
                        add = true
                        if(total) pofolService.getTotalPortfolio(getJwt()!!, lastPofolIdx)
                        else pofolService.getFollowPortfolio(getJwt()!!, lastPofolIdx)
                    }
                }
                else {
                    Log.d("SCROLL", "IDLE")
                }
            }
        })

        feedRVAdapter.setMyItemClickListener(object : CommunityFeedRVAdapter.MyItemListener{
            override fun urlParse(url: String): Uri {
                return Uri.parse(url)
            }

            override fun onShowComment(pofolIdx: Int) {  // 댓글 창 띄우기
                val intent = Intent(activity, PortfolioCommentActivity::class.java)
                intent.putExtra("pofolIdx", pofolIdx)
                startActivity(intent)
            }

            override fun onShowPopup(portfolio: GetPofolResult, position: Int, view: View) {
                showPopup(portfolio, position, view)
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

            override fun onLastPofolIndex(pofolIdx: Int) {
                lastPofolIdx = pofolIdx
            }
        })
    }

    private fun showPopup(portfolio: GetPofolResult, position: Int, view: View) {  // 내 댓글인 경우 삭제, 신고 둘 다 가능
        val themeWrapper = ContextThemeWrapper(context , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.portfolio_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            when(item!!.itemId) {
                R.id.portfolio_edit -> {  // 포트폴리오 수정하기
                    // 포폴 수정 창 띄우기
                    val intent = Intent(activity, PofolEditActivity::class.java)
                    intent.putExtra("pofolIdx", portfolio.pofolIdx)
                    intent.putExtra("title", portfolio.title)
                    intent.putExtra("content", portfolio.content)

                    startActivity(intent)
                }
                R.id.portfolio_delete -> {
                    val deleteDialog = BandDeleteDialog(getJwt()!!, getUserIdx(), portfolio.pofolIdx)
                    deleteDialog.show(parentFragmentManager, "deletePortfolio")

                    deleteDialog.setDialogListener(object : BandDeleteDialog.DeleteListener{
                        override fun deletePortfolio() {
                            feedRVAdapter.deleteFeed(position)
                        }
                    })
                }
                else -> {  // 포트폴리오 신고하기
                    Log.d("REPORT", "PORTFOLIO")
                }
            }
            false
        }

        if(portfolio.userIdx == getUserIdx()) {  // 내 포트폴리오인 경우
            popupMenu.menu.setGroupVisible(R.id.portfolio_report_gr, false)
        }
        else {  // 다른 사람 포트폴리오인 경우
            popupMenu.menu.setGroupVisible(R.id.portfolio_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.portfolio_delete_gr, false)
        }

        popupMenu.show() // 팝업 보여주기
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
        if(add) feedRVAdapter.addFeed(result)
        else initFeedRV(result)
    }

    override fun onGetTotalFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

    override fun onGetFollowSuccess(result: List<GetPofolResult>) {
        Log.d("GET/SUC", "$result")
        if(add) feedRVAdapter.addFeed(result)
        else initFeedRV(result)
    }

    override fun onGetFollowFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

}