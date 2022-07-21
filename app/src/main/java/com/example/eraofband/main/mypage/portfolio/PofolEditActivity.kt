package com.example.eraofband.main.mypage.portfolio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.eraofband.R
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityPofolEditBinding
import com.example.eraofband.remote.patchPofol.PatchPofolResponse
import com.example.eraofband.remote.patchPofol.PatchPofolService
import com.example.eraofband.remote.patchPofol.PatchPofolView
import kotlin.math.log

class PofolEditActivity : AppCompatActivity(), PatchPofolView {

    private lateinit var binding: ActivityPofolEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPofolEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioEditBackIb.setOnClickListener { finish() }

        Log.d("CHECKING", intent.getStringExtra("title").toString())

        // 내용 불러오기
        binding.portfolioEditTitleEt.setText(intent.getStringExtra("title").toString())
        binding.portfolioEditVideoIntroEt.setText(intent.getStringExtra("content").toString())

        binding.portfolioEditSaveBt.setOnClickListener {
            if(binding.portfolioEditTitleEt.text.isNotEmpty() && binding.portfolioEditVideoIntroEt.text.isNotEmpty()) {
                val title = binding.portfolioEditTitleEt.text.toString()
                val intro = binding.portfolioEditVideoIntroEt.text.toString()

                val patchService = PatchPofolService()
                patchService.setMakeView(this)
                patchService.makePortfolio(getJwt()!!, intent.getIntExtra("pofolIdx", 0), Portfolio(intro, "", title, getUserIdx(), ""))
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

    override fun onPatchSuccess(code: Int, result: String) {
        Log.d("PATCH/SUC", result)

        finish()
    }

    override fun onPatchFailure(response: PatchPofolResponse) {
        Log.d("PATCH/FAIL", "${response.code} ${response.message}")
    }
}