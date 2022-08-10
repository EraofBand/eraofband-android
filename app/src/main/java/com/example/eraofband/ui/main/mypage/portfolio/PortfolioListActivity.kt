package com.example.eraofband.ui.main.mypage.portfolio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityPortfolioListBinding
import com.example.eraofband.databinding.ItemPortfolioListBinding
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolView
import com.example.eraofband.ui.main.home.session.band.BandDeleteDialog
import com.example.eraofband.ui.main.mypage.MyPageActivity

class PortfolioListActivity : AppCompatActivity(), GetMyPofolView {

    private lateinit var binding : ActivityPortfolioListBinding
    private lateinit var portfolioAdapter : PortfolioListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioListBackIv.setOnClickListener { finish() }  // 뒤로 가기
        binding.portfolioListTitleTv.setOnClickListener {
            clearAll()
        }
    }

    override fun onResume() {
        super.onResume()

        val getMypofol = GetMyPofolService()
        getMypofol.setPofolView(this)
        getMypofol.getPortfolio(getUserIdx())
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRecyclerView(item: List<GetMyPofolResult>) {
        portfolioAdapter = PortfolioListRVAdapter(getJwt()!!, this)
        binding.portfolioListPortfolioRv.adapter = portfolioAdapter
        binding.portfolioListPortfolioRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        portfolioAdapter.initPortfolio(item)

        // 자기가 누른 포트폴리오로 넘어가는 거
        val smoothScroller: RecyclerView.SmoothScroller by lazy {
            object : LinearSmoothScroller(this) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = intent.getIntExtra("position", 0)
        binding.portfolioListPortfolioRv.layoutManager?.startSmoothScroll(smoothScroller)

        portfolioAdapter.setMyItemClickListener(object : PortfolioListRVAdapter.MyItemListener {
            override fun urlParse(url: String): Uri {
                return Uri.parse(url)
            }

            override fun onShowComment(pofolIdx: Int) {  // 댓글 창 띄우기
                val intent = Intent(this@PortfolioListActivity, PortfolioCommentActivity::class.java)
                intent.putExtra("pofolIdx", pofolIdx)
                startActivity(intent)
            }

            override fun onShowPopup(portfolio: GetMyPofolResult, position: Int, view: View) {
                showPopup(portfolio, position, view)
            }

            override fun onShowInfoPage(userIdx: Int) {
                // 내가 올린 포트폴리오기 때문에 무조건 내 정보를 띄움
                val intent = Intent(this@PortfolioListActivity, MyPageActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }
        })
    }

    override fun onGetSuccess(result: List<GetMyPofolResult>) {
        Log.d("MYPORTFOLIO/FAIL", result.toString())
        initRecyclerView(result)
        result[0].userIdx
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPORTFOLIO/FAIL", "$code $message")
    }

    private fun showPopup(portfolio: GetMyPofolResult, position: Int, view: View) {  // 내 댓글인 경우 삭제, 신고 둘 다 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.portfolio_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            when(item!!.itemId) {
                R.id.portfolio_edit -> {  // 포트폴리오 수정하기
                    // 포폴 수정 창 띄우기
                    val intent = Intent(this@PortfolioListActivity, PofolEditActivity::class.java)
                    intent.putExtra("pofolIdx", portfolio.pofolIdx)
                    intent.putExtra("title", portfolio.title)
                    intent.putExtra("content", portfolio.content)

                    startActivity(intent)
                }
                R.id.portfolio_delete -> {
                    val deleteDialog = BandDeleteDialog(getJwt()!!, getUserIdx(), portfolio.pofolIdx)
                    deleteDialog.show(supportFragmentManager, "deletePortfolio")

                    deleteDialog.setDialogListener(object : BandDeleteDialog.DeleteListener{
                        override fun deletePortfolio() {
                            portfolioAdapter.deletePortfolio(position)
                        }
                    })
                }
                else -> {  // 포트폴리오 신고하기
                    Log.d("REPORT", "PORTFOLIO")
                }
            }
            false
        }

        // 여기는 무조건 내 포트폴리오만 나오기 때문에 내 포트폴리오인 경우만 고려
        popupMenu.menu.setGroupVisible(R.id.portfolio_report_gr, false)
        popupMenu.show() // 팝업 보여주기
    }

    override fun onBackPressed() {
        super.onBackPressed()
        clearAll()
    }

    private fun clearAll() {
        portfolioAdapter.clearVideo()
    }
}