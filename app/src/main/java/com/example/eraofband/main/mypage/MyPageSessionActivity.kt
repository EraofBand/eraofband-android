package com.example.eraofband.main.mypage

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.eraofband.data.Session
import com.example.eraofband.databinding.ActivityMyPageSessionBinding
import com.example.eraofband.remote.checkUser.CheckUserService
import com.example.eraofband.remote.patchSession.PatchSessionService
import com.example.eraofband.remote.patchSession.PatchSessionView

class MyPageSessionActivity : AppCompatActivity(), PatchSessionView{

    lateinit var binding : ActivityMyPageSessionBinding
    var session = Session(-1,-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageSessionBinding.inflate(layoutInflater)
        session.userIdx = getUserIdx()

        setContentView(binding.root)

        // 원래 나의 세션이 뭐였는지 체크
        var editSession = intent.getIntExtra("session", -1)

        when(editSession){
            0 -> binding.mypageSessionVocalCb.isChecked = true
            1 -> binding.mypageSessionGuitarCb.isChecked = true
            2 -> binding.mypageSessionBaseCb.isChecked = true
            3 -> binding.mypageSessionKeyboardCb.isChecked = true
            4 -> binding.mypageSessionDrumCb.isChecked = true
        }

        binding.mypageSessionBackIv.setOnClickListener{
            finish()
        }

        binding.mypageSessionNextBtn.setOnClickListener{ // 변경버튼 api 연동
            val patchSessionService = PatchSessionService()
            patchSessionService.setPatchSessionView(this)
            patchSessionService.patchSession(getJwt()!!, session)
            finish()
        }

        binding.mypageSessionVocalCb.setOnClickListener {
            session.session = 0
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
            session.session = 1

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
            session.session = 2

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
            session.session = 3

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
            session.session = 4

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
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onPatchSessionSuccess(result: String) {
        Log.d("SESSIOM/SUCCESS", result)
    }

    override fun onPatchSessionFailure(code: Int, message: String) {
        Log.d("SESSION/FAIL", "$code $message")
    }
}