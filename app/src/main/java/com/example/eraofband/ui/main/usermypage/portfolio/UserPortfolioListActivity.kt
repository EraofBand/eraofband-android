package com.example.eraofband.ui.main.usermypage.portfolio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityUserPortfolioListBinding
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolView
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioCommentActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import com.example.eraofband.ui.report.ReportDialog
import android.view.View as View1

class UserPortfolioListActivity : AppCompatActivity(), GetMyPofolView {

    private lateinit var binding : ActivityUserPortfolioListBinding
    private lateinit var userPortfolioAdapter : UserPortfolioListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserPortfolioListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userPortfolioListBackIv.setOnClickListener {   // 뒤로 가기
            initRecyclerView(arrayListOf())
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        val getMypofol = GetMyPofolService()
        getMypofol.setPofolView(this)
        getMypofol.getPortfolio(getJwt()!!, getUserIdx())
    }

    private fun getUserIdx(): Int {
        val intent = intent
        return intent.extras?.getInt("userIdx")!!
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRecyclerView(item: List<GetPofolResult>) {
        userPortfolioAdapter = UserPortfolioListRVAdapter(getJwt()!!, this)
        binding.userPortfolioListPortfolioRv.adapter = userPortfolioAdapter
        binding.userPortfolioListPortfolioRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        userPortfolioAdapter.initPortfolio(item)

        // 자기가 누른 포트폴리오로 넘어가는 거
        val smoothScroller: RecyclerView.SmoothScroller by lazy {
            object : LinearSmoothScroller(this) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = intent.getIntExtra("position", 0)
        binding.userPortfolioListPortfolioRv.layoutManager?.startSmoothScroll(smoothScroller)

        userPortfolioAdapter.setMyItemClickListener(object :
            UserPortfolioListRVAdapter.MyItemListener {
            override fun urlParse(url: String): Uri {
                return Uri.parse(url)
            }

            override fun onShowComment(pofolIdx: Int) {  // 댓글 창 띄우기
                val intent = Intent(this@UserPortfolioListActivity, PortfolioCommentActivity::class.java)
                intent.putExtra("pofolIdx", pofolIdx)
                startActivity(intent)
            }

            override fun onShowPopup(portfolio: GetPofolResult, view: View1) {
                 showPopup(portfolio, view)  // 다른 사람이 단 댓글
            }

            override fun onShowInfoPage(userIdx: Int) {
                // 다른 사람들이 올린 포트폴리오기 때문에 무조건 다른 사람 정보를 띄움
                val intent = Intent(this@UserPortfolioListActivity, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }
        })
    }

    private fun showPopup(portfolio: GetPofolResult, view: android.view.View) {  // 내 댓글인 경우 삭제, 신고 둘 다 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.portfolio_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.portfolio_report -> {
                    // 포트폴리오 신고하기
                    val reportDialog = ReportDialog(getJwt()!!, 1, portfolio.pofolIdx, portfolio.userIdx)
                    reportDialog.isCancelable = false
                    reportDialog.show(supportFragmentManager, "report")
                }
            }
            false
        }

        // 여기는 무조건 다른 사람 포트폴리오만 나오기 때문에 다른 사람 포트폴리오인 경우만 고려
        popupMenu.menu.setGroupVisible(R.id.portfolio_edit_gr, false)
        popupMenu.menu.setGroupVisible(R.id.portfolio_delete_gr, false)

        popupMenu.show() // 팝업 보여주기
    }

    override fun onGetSuccess(result: List<GetPofolResult>) {
        Log.d("MYPORTFOLIO/FAIL", result.toString())
        initRecyclerView(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPORTFOLIO/FAIL", "$code $message")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        initRecyclerView(arrayListOf())
    }
}