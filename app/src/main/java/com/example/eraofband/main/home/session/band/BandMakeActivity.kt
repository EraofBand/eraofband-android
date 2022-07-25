package com.example.eraofband.main.home.session.band

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityBandMakeBinding

class BandMakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBandMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandMakeBackIb.setOnClickListener { finish() }
    }
}