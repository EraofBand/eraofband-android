package com.example.eraofband.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityHomeBandMakeBinding

class HomeBandMakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBandMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBandMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandMakeBackIb.setOnClickListener { finish() }
    }
}