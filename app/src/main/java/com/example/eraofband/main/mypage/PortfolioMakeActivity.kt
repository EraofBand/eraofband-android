package com.example.eraofband.main.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityPortfolioMakeBinding


class PortfolioMakeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPortfolioMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}