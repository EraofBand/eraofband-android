package com.example.eraofband.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityHomeBandEditBinding

class HomeBandEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBandEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBandEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandEditBackIb.setOnClickListener { finish() }
    }
}