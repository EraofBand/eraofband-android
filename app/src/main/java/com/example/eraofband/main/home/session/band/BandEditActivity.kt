package com.example.eraofband.main.home.session.band

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityBandEditBinding

class BandEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBandEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBandEditBackIb.setOnClickListener { finish() }
    }
}