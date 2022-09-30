package com.example.eraofband.ui.main.block

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityUserBlockBinding

class UserBlockActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUserBlockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}