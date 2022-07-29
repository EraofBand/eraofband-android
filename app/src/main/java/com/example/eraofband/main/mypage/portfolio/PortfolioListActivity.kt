package com.example.eraofband.main.mypage.portfolio

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
import com.example.eraofband.main.usermypage.UserMyPageActivity
import com.example.eraofband.remote.deletePofol.DeletePofolResponse
import com.example.eraofband.remote.deletePofol.DeletePofolService
import com.example.eraofband.remote.deletePofol.DeletePofolView
import com.example.eraofband.remote.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.getMyPofol.GetMyPofolService
import com.example.eraofband.remote.getMyPofol.GetMyPofolView

class PortfolioListActivity : AppCompatActivity(), GetMyPofolView, DeletePofolView {

    private lateinit var binding : ActivityPortfolioListBinding
    private lateinit var portfolioAdapter : PortfolioListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioListBackIv.setOnClickListener { finish() }  // 뒤로 가기

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
                val intent = Intent(this@PortfolioListActivity, UserMyPageActivity::class.java)
                intent.putExtra("comment", userIdx)
                startActivity(intent)
            }
        })
    }

    override fun onGetSuccess(result: List<GetMyPofolResult>) {
        Log.d("MYPORTFOLIO/FAIL", result.toString())
        initRecyclerView(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPORTFOLIO/FAIL", "$code $message")
    }

    private fun showPopup(portfolio: GetMyPofolResult, position: Int, view: View) {  // 내 댓글인 경우 삭제, 신고 둘 다 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.portfolio_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.portfolio_edit) {  // 포트폴리오 수정하기
                // 포폴 수정 창 띄우기
                val intent = Intent(this@PortfolioListActivity, PofolEditActivity::class.java)
                intent.putExtra("pofolIdx", portfolio.pofolIdx)
                intent.putExtra("title", portfolio.title)
                intent.putExtra("content", portfolio.content)

                startActivity(intent)
            }
            else if (item.itemId == R.id.portfolio_delete) {  // 포트폴리오 삭제하기
                // position을 넘겨줌 이거 말고 생각이 안나요ㅠㅠ
                val portfolioSP = getSharedPreferences("portfolio", MODE_PRIVATE)
                val editor = portfolioSP.edit()

                editor.putInt("position", position)
                editor.apply()

                // 포폴 삭제
                val deletePofolService = DeletePofolService()
                deletePofolService.setDeleteView(this)
                deletePofolService.deletePortfolio(getJwt()!!, portfolio.pofolIdx, getUserIdx())
            }
            else {  // 포트폴리오 신고하기
                Log.d("REPORT", "PORTFOLIO")
            }

            false
        }

        // 여기는 무조건 내 포트폴리오만 나오기 때문에 내 포트폴리오인 경우만 고려
        popupMenu.menu.setGroupVisible(R.id.portfolio_report_gr, false)

        popupMenu.show() // 팝업 보여주기
    }

    override fun onDeleteSuccess(code: Int, result: String) {  // 포트폴리오 삭제
        Log.d("DELETE/SUC", result)
        val portfolioSP = getSharedPreferences("portfolio", MODE_PRIVATE)

        // 리사이클러뷰에서도 삭제
        portfolioAdapter.deletePortfolio(portfolioSP.getInt("position", 0))

        // 사용한 position은 지워줌
        val editor = portfolioSP.edit()
        editor.remove("position")
        editor.apply()
    }

    override fun onDeleteFailure(response: DeletePofolResponse) {
        Log.d("DELETE/FAIL", response.toString())
    }
}