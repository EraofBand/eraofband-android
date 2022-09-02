package com.example.eraofband.ui.main.mypage.portfolio

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityPofolEditBinding
import com.example.eraofband.remote.portfolio.patchPofol.PatchPofolResponse
import com.example.eraofband.remote.portfolio.patchPofol.PatchPofolService
import com.example.eraofband.remote.portfolio.patchPofol.PatchPofolView

class PofolEditActivity : AppCompatActivity(), PatchPofolView {

    private lateinit var binding: ActivityPofolEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPofolEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patchService = PatchPofolService()
        patchService.setPatchView(this)

        binding.portfolioEditBackIb.setOnClickListener { finish() }

        // 내용 불러오기
        binding.portfolioEditTitleEt.setText(intent.getStringExtra("title").toString())
        binding.portfolioEditVideoIntroEt.setText(intent.getStringExtra("content").toString())

        binding.portfolioEditSaveBt.setOnClickListener {
            if(binding.portfolioEditTitleEt.text.isNotEmpty() && binding.portfolioEditVideoIntroEt.text.isNotEmpty()) {
                val title = "${binding.portfolioEditTitleEt.text.trim()}"
                val intro = "${binding.portfolioEditVideoIntroEt.text.trim()}"

                patchService.patchPortfolio(getJwt()!!, intent.getIntExtra("pofolIdx", 0), Portfolio(intro, "", title, getUserIdx(), ""))
            }
        }
    }

    private fun getUserIdx() : Int {  // 내 userIdx를 불러옴
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {  // 내 jwt를 불러옴
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onPatchSuccess(code: Int, result: String) {
        Log.d("PATCH/SUC", result)

        finish()
    }

    override fun onPatchFailure(response: PatchPofolResponse) {
        Log.d("PATCH/FAIL", "${response.code} ${response.message}")
    }
}