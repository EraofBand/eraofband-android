package com.example.eraofband.ui.main.mypage.portfolio

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        binding.portfolioEditBackIb.setOnClickListener { finish() }

        binding.root.setOnClickListener {
            if(binding.portfolioEditTitleEt.isFocused) hideKeyboard()
            else if(binding.portfolioEditVideoIntroEt.isFocused) hideKeyboard()
        }

        // 내용 불러오기
        binding.portfolioEditTitleEt.setText(intent.getStringExtra("title").toString())
        binding.portfolioEditVideoIntroEt.setText(intent.getStringExtra("content").toString())

        binding.portfolioEditSaveBt.setOnClickListener {
            if(binding.portfolioEditTitleEt.text.isNotEmpty() && binding.portfolioEditVideoIntroEt.text.isNotEmpty()) {
                val title = binding.portfolioEditTitleEt.text.toString()
                val intro = binding.portfolioEditVideoIntroEt.text.toString()

                val patchService = PatchPofolService()
                patchService.setPatchView(this)
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

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onPatchSuccess(code: Int, result: String) {
        Log.d("PATCH/SUC", result)

        finish()
    }

    override fun onPatchFailure(response: PatchPofolResponse) {
        Log.d("PATCH/FAIL", "${response.code} ${response.message}")
    }
}