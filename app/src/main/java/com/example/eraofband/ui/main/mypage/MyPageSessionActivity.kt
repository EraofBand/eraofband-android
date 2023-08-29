package com.example.eraofband.ui.main.mypage

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.data.Report
import com.example.eraofband.data.Session
import com.example.eraofband.databinding.ActivityMyPageSessionBinding
import com.example.eraofband.remote.user.patchSession.PatchSessionService
import com.example.eraofband.remote.user.patchSession.PatchSessionView
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.ui.setOnSingleClickListener

class MyPageSessionActivity : AppCompatActivity(), PatchSessionView, RefreshJwtView{

    lateinit var binding : ActivityMyPageSessionBinding
    var session = Session(-1,-1)

    private val patchSessionService = PatchSessionService()
    private val refreshJwtService = RefreshJwtService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session.userIdx = getUserIdx()

        patchSessionService.setPatchSessionView(this)
        refreshJwtService.setRefreshView(this)

        // 원래 나의 세션이 뭐였는지 체크
        when(intent.getIntExtra("session", -1)){
            0 -> binding.mypageSessionVocalCb.isChecked = true
            1 -> binding.mypageSessionGuitarCb.isChecked = true
            2 -> binding.mypageSessionBaseCb.isChecked = true
            3 -> binding.mypageSessionKeyboardCb.isChecked = true
            4 -> binding.mypageSessionDrumCb.isChecked = true
        }

        binding.mypageSessionBackIv.setOnClickListener{
            finish()
        }

        binding.mypageSessionNextBtn.setOnSingleClickListener{ // 변경버튼 api 연동
            if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
                refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, getUserIdx())
            } else {
                patchSessionService.patchSession(getJwt()!!, session)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 100)
        }

        binding.mypageSessionVocalCb.setOnClickListener {
            session.userSession = 0
            binding.mypageSessionVocalCb.isChecked = true
            binding.mypageSessionGuitarCb.isChecked = false
            binding.mypageSessionBaseCb.isChecked = false
            binding.mypageSessionKeyboardCb.isChecked = false
            binding.mypageSessionDrumCb.isChecked = false

            binding.mypageSessionVocalTv.setTextColor(Color.parseColor("#1864FD"))
            binding.mypageSessionGuitarTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionBaseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionKeyboardTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionDrumTv.setTextColor(Color.parseColor("#FFFFFF"))

        }

        binding.mypageSessionGuitarCb.setOnClickListener {
            session.userSession = 1

            binding.mypageSessionVocalCb.isChecked = false
            binding.mypageSessionGuitarCb.isChecked = true
            binding.mypageSessionBaseCb.isChecked = false
            binding.mypageSessionKeyboardCb.isChecked = false
            binding.mypageSessionDrumCb.isChecked = false

            binding.mypageSessionVocalTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionGuitarTv.setTextColor(Color.parseColor("#1864FD"))
            binding.mypageSessionBaseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionKeyboardTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionDrumTv.setTextColor(Color.parseColor("#FFFFFF"))
        }

        binding.mypageSessionBaseCb.setOnClickListener {
            session.userSession = 2

            binding.mypageSessionVocalCb.isChecked = false
            binding.mypageSessionGuitarCb.isChecked = false
            binding.mypageSessionBaseCb.isChecked = true
            binding.mypageSessionKeyboardCb.isChecked = false
            binding.mypageSessionDrumCb.isChecked = false

            binding.mypageSessionVocalTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionGuitarTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionBaseTv.setTextColor(Color.parseColor("#1864FD"))
            binding.mypageSessionKeyboardTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionDrumTv.setTextColor(Color.parseColor("#FFFFFF"))
        }

        binding.mypageSessionKeyboardCb.setOnClickListener {
            session.userSession = 3

            binding.mypageSessionVocalCb.isChecked = false
            binding.mypageSessionGuitarCb.isChecked = false
            binding.mypageSessionBaseCb.isChecked = false
            binding.mypageSessionKeyboardCb.isChecked = true
            binding.mypageSessionDrumCb.isChecked = false

            binding.mypageSessionVocalTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionGuitarTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionBaseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionKeyboardTv.setTextColor(Color.parseColor("#1864FD"))
            binding.mypageSessionDrumTv.setTextColor(Color.parseColor("#FFFFFF"))
        }

        binding.mypageSessionDrumCb.setOnClickListener {
            session.userSession = 4

            binding.mypageSessionVocalCb.isChecked = false
            binding.mypageSessionGuitarCb.isChecked = false
            binding.mypageSessionBaseCb.isChecked = false
            binding.mypageSessionKeyboardCb.isChecked = false
            binding.mypageSessionDrumCb.isChecked = true

            binding.mypageSessionVocalTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionGuitarTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionBaseTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionKeyboardTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.mypageSessionDrumTv.setTextColor(Color.parseColor("#1864FD"))
        }
    }
    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return getSharedPreferences("user", MODE_PRIVATE)
    }

    override fun onPatchSessionSuccess(result: String) {
        Log.d("SESSION/SUCCESS", result)
    }

    override fun onPatchSessionFailure(code: Int, message: String) {
        Log.d("SESSION/FAIL", "$code $message")
    }

    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")
        patchSessionService.patchSession(getJwt()!!, session)
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REFRESH/FAIL", "$code $message")
    }
}