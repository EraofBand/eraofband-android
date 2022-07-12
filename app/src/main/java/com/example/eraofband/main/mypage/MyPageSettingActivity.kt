package com.example.eraofband.main.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityMypageSettingBinding


class MyPageSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBackIb.setOnClickListener{
            finish()
        }
    }
}