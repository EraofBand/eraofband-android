package com.example.eraofband.ui.main.mypage.portfolio

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolService
import com.example.eraofband.remote.portfolio.getPofol.GetMyPofolView
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.ui.main.home.session.band.BandDeleteDialog
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.FeedTemplate

class PortfolioListActivity : AppCompatActivity(), GetMyPofolView {

    private lateinit var binding : ActivityPortfolioListBinding
    private lateinit var portfolioAdapter : PortfolioListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioListBackIv.setOnClickListener {   // 뒤로 가기
            initRecyclerView(arrayListOf())
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val getMypofol = GetMyPofolService()
        getMypofol.setPofolView(this)
        getMypofol.getPortfolio(getJwt()!!, getUserIdx())
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRecyclerView(item: List<GetPofolResult>) {
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

            override fun onShowPopup(portfolio: GetPofolResult, position: Int, view: View) {
                showPopup(portfolio, position, view)
            }

            override fun onShowInfoPage(userIdx: Int) {
                // 내가 올린 포트폴리오기 때문에 무조건 내 정보를 띄움
                val intent = Intent(this@PortfolioListActivity, MyPageActivity::class.java)
                intent.putExtra("userIdx", userIdx)
                startActivity(intent)
            }

            override fun sharePofol(defaultFeed: FeedTemplate) {
                // 피드 메시지 보내기
                // 카카오톡 설치여부 확인
                binding.portfolioListPb.visibility = View.VISIBLE

                if (ShareClient.instance.isKakaoTalkSharingAvailable(this@PortfolioListActivity)) {
                    // 카카오톡으로 카카오톡 공유 가능
                    ShareClient.instance.shareDefault(this@PortfolioListActivity, defaultFeed) { sharingResult, error ->
                        if (error != null) {
                            Log.e("SHARE", "카카오톡 공유 실패", error)
                        }
                        else if (sharingResult != null) {
                            Log.d("SHARE", "카카오톡 공유 성공 ${sharingResult.intent}")
                            startActivity(sharingResult.intent)

                            // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                            Log.w("SHARE", "Warning Msg: ${sharingResult.warningMsg}")
                            Log.w("SHARE", "Argument Msg: ${sharingResult.argumentMsg}")
                        }
                    }
                } else {
                    // 카카오톡 미설치: 웹 공유 사용 권장
                    // 웹 공유 예시 코드
                    val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

                    // CustomTabs으로 웹 브라우저 열기

                    // 1. CustomTabsServiceConnection 지원 브라우저 열기
                    // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
                    try {
                        KakaoCustomTabsClient.openWithDefault(this@PortfolioListActivity, sharerUrl)
                    } catch(e: UnsupportedOperationException) {
                        // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                    }

                    // 2. CustomTabsServiceConnection 미지원 브라우저 열기
                    // ex) 다음, 네이버 등
                    try {
                        KakaoCustomTabsClient.open(this@PortfolioListActivity, sharerUrl)
                    } catch (e: ActivityNotFoundException) {
                        // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.portfolioListPb.visibility = View.GONE
                }, 3000)
            }
        })
    }

    override fun onGetSuccess(result: List<GetPofolResult>) {
        Log.d("MYPORTFOLIO/FAIL", result.toString())
        initRecyclerView(result)
        result[0].userIdx
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPORTFOLIO/FAIL", "$code $message")
    }

    private fun showPopup(portfolio: GetPofolResult, position: Int, view: View) {  // 내 댓글인 경우 삭제, 신고 둘 다 가능
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
            }
            false
        }

        // 여기는 무조건 내 포트폴리오만 나오기 때문에 내 포트폴리오인 경우만 고려
        popupMenu.menu.setGroupVisible(R.id.portfolio_report_gr, false)
        popupMenu.show() // 팝업 보여주기
    }

    override fun onBackPressed() {
        super.onBackPressed()
        initRecyclerView(arrayListOf())
    }
}